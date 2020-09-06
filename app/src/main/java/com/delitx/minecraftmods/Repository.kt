package com.delitx.minecraftmods

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.delitx.minecraftmods.data.dao.FavouritesDao
import com.delitx.minecraftmods.data.dao.MapsDao
import com.delitx.minecraftmods.data.dao.ModsDao
import com.delitx.minecraftmods.data.dao.SkinsDao
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.network.FirebaseRequests
import com.delitx.minecraftmods.network.FirebaseRequestsImpl
import com.delitx.minecraftmods.pojo.*
import com.delitx.minecraftmods.pojo.Map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.*


class Repository private constructor(
    private val mDatabase: DataBase,
    private val app: Application
) :
    FirebaseRequestsImpl.FirebaseInteraction {
    private val MOD_SHARED_PREFS = "mod"
    private val MAP_SHARED_PREFS = "map"
    private val SKIN_SHARED_PREFS = "skin"
    private val SKIN_LOCATION = "/games/com.mojang/minecraftpe/custom.png"
    private val MINECRAFT_PACKAGE_NAME = "com.mojang.minecraftpe"
    private val MINECRAFT_LINK =
        "https://play.google.com/store/apps/details?id=$MINECRAFT_PACKAGE_NAME"
    private val mModsDao: ModsDao = mDatabase.getModsDao()
    private val mMapsDao: MapsDao = mDatabase.getMapsDao()
    private val mSkinsDao: SkinsDao = mDatabase.getSkinsDao()
    private val mFavouritesDao: FavouritesDao = mDatabase.getFavouritesDao()
    private val mFirebaseRequests: FirebaseRequests = FirebaseRequestsImpl(this)
    private var mLanguage: String = Locale.getDefault().language

    val modCategories: MutableLiveData<List<Category>> = MutableLiveData(
        listOf(
            Category(
                real = "",
                shown = app.resources.getString(R.string.all)
            )
        )
    )
    val mapCategories: MutableLiveData<List<Category>> = MutableLiveData(
        listOf(
            Category(
                real = "",
                shown = app.resources.getString(R.string.all)
            )
        )
    )
    val skinCategories: MutableLiveData<List<Category>> = MutableLiveData(
        listOf(
            Category(
                real = "",
                shown = app.resources.getString(R.string.all)
            )
        )
    )

    companion object {
        private var mInstance: Repository? = null
        fun getInstance(database: DataBase, app: Application): Repository {
            if (mInstance == null) {
                mInstance = Repository(database, app)
            }
            return mInstance!!
        }
    }

    init {
        refreshMapCategories()
        refreshModCategories()
        refreshSkinCategories()
    }

    fun refreshMapCategories() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        CoroutineScope(Default).launch {
            val map = sharedPrefs.getString(MAP_SHARED_PREFS, "")
            val categoriesMap = map!!.decodeCategories()
            withContext(Main) {
                setMapsCategories(categoriesMap)
            }
        }
    }

    fun refreshModCategories() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        CoroutineScope(Default).launch {
            val mod = sharedPrefs.getString(MOD_SHARED_PREFS, "")
            val categoriesMod = mod!!.decodeCategories()
            withContext(Main) {
                setModsCategories(categoriesMod)
            }
        }
    }

    fun refreshSkinCategories() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        CoroutineScope(Default).launch {
            val skin = sharedPrefs.getString(SKIN_SHARED_PREFS, "")
            val categoriesSkin = skin!!.decodeCategories()
            withContext(Main) {
                setSkinsCategories(categoriesSkin)
            }
        }
    }


    fun getJSONs() {
        mFirebaseRequests.getJSONs()
    }

    fun recreateDB() {
        recreateModDB()
        recreateMapDB()
        recreateSkinDB()
    }

    fun getMapFavourites(): LiveData<List<Map>> {
        return mMapsDao.selectFavourite()
    }

    fun getModFavourites(): LiveData<List<Mod>> {
        return mModsDao.selectFavourite()
    }

    fun getSkinFavourites(): LiveData<List<Skin>> {
        return mSkinsDao.selectFavourite()
    }

    fun searchMap(query: String): LiveData<List<Map>> {
        return mMapsDao.search(query)
    }

    fun searchMod(query: String): LiveData<List<Mod>> {
        return mModsDao.search(query)
    }

    fun likeMod(mod: Mod) {
        CoroutineScope(IO).launch {
            mFavouritesDao.insert(Favourites(mod.id))
            mod.isFavourite = true
            mModsDao.insert(mod)
        }
    }

    fun likeMap(map: Map) {
        CoroutineScope(IO).launch {
            mFavouritesDao.insert(Favourites(map.id))
            map.isFavourite = true
            mMapsDao.insert(map)
        }
    }

    fun likeSkin(skin: Skin) {
        CoroutineScope(IO).launch {
            mFavouritesDao.insert(Favourites(skin.id))
            skin.isFavourite = true
            mSkinsDao.insert(skin)
        }
    }

    fun dislikeMod(mod: Mod) {
        CoroutineScope(IO).launch {
            mFavouritesDao.delete(Favourites(mod.id))
            mod.isFavourite = false
            mModsDao.insert(mod)
        }
    }

    fun dislikeMap(map: Map) {
        CoroutineScope(IO).launch {
            mFavouritesDao.delete(Favourites(map.id))
            map.isFavourite = false
            mMapsDao.insert(map)
        }
    }

    fun dislikeSkin(skin: Skin) {
        CoroutineScope(IO).launch {
            mFavouritesDao.delete(Favourites(skin.id))
            skin.isFavourite = false
            mSkinsDao.insert(skin)
        }
    }

    fun List<BaseClass>.markFavourite(favourites: MutableList<Favourites>) {
        for (i in this) {
            val id = i.id
            for (f in favourites) {
                if (id == f.id) {
                    i.isFavourite = true
                    favourites.remove(f)
                    break
                }
            }
        }
    }

    private fun recreateModDB() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        mLanguage = Locale.getDefault().language
        CoroutineScope(IO).launch {
            val mod = sharedPrefs.getString(MOD_SHARED_PREFS, "")
            if (mod != "") {
                mModsDao.deleteAll()
                val favourites = mFavouritesDao.selectAll().toMutableList()
                val mods = mod!!.decodeToModList()
                mods.markFavourite(favourites)
                mModsDao.insert(mods)
            }
        }
    }

    private fun recreateMapDB() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        mLanguage = Locale.getDefault().language
        CoroutineScope(IO).launch {
            val map = sharedPrefs.getString(MAP_SHARED_PREFS, "")
            if (map != "") {
                mMapsDao.deleteAll()
                val favourites = mFavouritesDao.selectAll().toMutableList()
                val items = map!!.decodeToMapList()
                items.markFavourite(favourites)
                mMapsDao.insert(items)
            }
        }
    }

    private fun recreateSkinDB() {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        mLanguage = Locale.getDefault().language
        CoroutineScope(Default).launch {
            val skin = sharedPrefs.getString(SKIN_SHARED_PREFS, "")
            if (skin != "") {
                mSkinsDao.deleteAll()
                val favourites = mFavouritesDao.selectAll().toMutableList()
                val items = skin!!.decodeToSkinList()
                items.markFavourite(favourites)
                mSkinsDao.insert(items)
            }
        }
    }


    private fun String.decodeToImagesList(prefix: String = ""): List<String> {
        try {
            val json = JSONArray(this)
            val length = json.length()
            val result = mutableListOf<String>()
            for (i in 0 until length) {
                val value = json[i]
                if (value is JSONObject) {
                    val image = try {
                        value["image"]
                    } catch (e: Exception) {
                        continue
                    }.toString()
                    result.add(prefix + image)
                }
            }
            return result
        } catch (e: Exception) {
            return listOf()
        }
    }

    private fun String.decodeCategories(): List<Category> {
        return try {
            val json = JSONArray(this)
            val length = json.length()
            val set = mutableSetOf<Category>()
            for (i in 0 until length) {
                val value = json[i]
                if (value is JSONObject) {
                    val shown: String = try {
                        value["category_$mLanguage"]
                    } catch (e: Exception) {
                        value["category_def"]
                    }.toString()
                    val real = try {
                        value["category_def"]
                    } catch (e: Exception) {
                        //skipping because file path is primary part of item
                        continue
                    }.toString()
                    set.add(
                        Category(
                            real, shown
                        )
                    )
                }
            }
            val result = mutableListOf<Category>()
            result.addAll(set)
            val indexOfEmpty = result.indexOf(Category("", ""))
            if (indexOfEmpty != -1) {
                result.removeAt(indexOfEmpty)
            }
            return result
        } catch (e: java.lang.Exception) {
            listOf()
        }
    }

    private fun String.decodeToModList(): List<Mod> {
        try {
            val json = JSONArray(this)
            val length = json.length()
            val result = mutableListOf<Mod>()
            for (i in 0 until length) {
                val value = json[i]
                if (value is JSONObject) {
                    val name = try {
                        value["title_$mLanguage"]
                    } catch (e: Exception) {
                        value["title_def"]
                    }.toString()
                    val description: String = try {
                        value["desc_$mLanguage"]
                    } catch (e: Exception) {
                        value["desc_def"]
                    }.toString()
                    val filePath = try {
                        "mods/" + value["file"]
                    } catch (e: Exception) {
                        //skipping because file path is primary part of item
                        continue
                    }.toString()
                    val category = try {
                        value["category_def"].toString()
                    } catch (e: java.lang.Exception) {
                        ""
                    }
                    val images: List<String> =
                        value["images"].toString().decodeToImagesList("mods/")
                    result.add(
                        Mod(
                            id = filePath,
                            images = images,
                            name = name,
                            description = description,
                            place = i,
                            category = category
                        )
                    )
                }
            }
            return result
        } catch (e: Exception) {
            return listOf()
        }
    }

    private fun String.decodeToMapList(): List<Map> {
        try {
            val json = JSONArray(this)
            val length = json.length()
            val result = mutableListOf<Map>()
            for (i in 0 until length) {
                val value = json[i]
                if (value is JSONObject) {
                    val name = try {
                        value["title_$mLanguage"]
                    } catch (e: Exception) {
                        value["title_def"]
                    }.toString()
                    val description: String = try {
                        value["desc_$mLanguage"]
                    } catch (e: Exception) {
                        value["desc_def"]
                    }.toString()
                    val filePath = try {
                        "maps/" + value["file"]
                    } catch (e: Exception) {
                        //skipping because file path is primary part of item
                        continue
                    }.toString()
                    val category = try {
                        value["category_def"].toString()
                    } catch (e: java.lang.Exception) {
                        ""
                    }
                    val images: List<String> =
                        value["images"].toString().decodeToImagesList("maps/")
                    result.add(
                        Map(
                            id = filePath,
                            images = images,
                            name = name,
                            description = description,
                            place = i,
                            category = category
                        )
                    )
                }
            }
            return result
        } catch (e: Exception) {
            return listOf()
        }
    }

    private fun String.decodeToSkinList(): List<Skin> {
        try {
            val json = JSONArray(this)
            val length = json.length()
            val result = mutableListOf<Skin>()
            for (i in 0 until length) {
                val value = json[i]
                if (value is JSONObject) {
                    val filePath = try {
                        "skins/" + value["file"]
                    } catch (e: Exception) {
                        //skipping because file path is primary part of item
                        continue
                    }.toString()
                    val images: List<String> =
                        value["images"].toString().decodeToImagesList("skins/")
                    val category = try {
                        value["category_def"].toString()
                    } catch (e: java.lang.Exception) {
                        ""
                    }
                    result.add(
                        Skin(
                            id = filePath,
                            images = images,
                            place = i,
                            category = category
                        )
                    )
                }
            }
            return result
        } catch (e: Exception) {
            return listOf()
        }
    }


    suspend fun getMod(modId: String): Mod {
        return mModsDao.getMod(modId)
    }

    suspend fun getMap(id: String): Map {
        return mMapsDao.getMap(id)
    }

    suspend fun getSkin(id: String): Skin {
        return mSkinsDao.getSkin(id)
    }

    fun downloadPhoto(link: String, imageHolder: ImageView) {
        mFirebaseRequests.loadImage(link, imageHolder)
    }

    fun getMods(category: String = ""): LiveData<List<Mod>> =
        if (category != "") mModsDao.getByCategory(category) else mModsDao.getAll()

    fun getMaps(category: String = ""): LiveData<List<Map>> =
        if (category != "") mMapsDao.getByCategory(category) else mMapsDao.getAll()

    fun getSkins(category: String = ""): LiveData<List<Skin>> =
        if (category != "") mSkinsDao.getByCategory(category) else mSkinsDao.getAll()


    private fun setModsCategories(categories: List<Category>) {
        val value =
            listOf(Category(real = "", shown = app.resources.getString(R.string.all))) + categories
        modCategories.postValue(value)
    }

    private fun setMapsCategories(categories: List<Category>) {
        val value =
            listOf(Category(real = "", shown = app.resources.getString(R.string.all))) + categories
        mapCategories.postValue(value)
    }

    private fun setSkinsCategories(categories: List<Category>) {
        val value =
            listOf(Category(real = "", shown = app.resources.getString(R.string.all))) + categories
        skinCategories.postValue(value)
    }

    override fun saveMapJSON(json: String) {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(MAP_SHARED_PREFS, json)
        editor.apply()
        recreateMapDB()
    }

    override fun saveModJSON(json: String) {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(MOD_SHARED_PREFS, json)
        editor.apply()
        recreateModDB()
    }

    override fun saveSkinJSON(json: String) {
        val sharedPrefs = app.getSharedPreferences("shared_prefs", Application.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(SKIN_SHARED_PREFS, json)
        editor.apply()
        recreateSkinDB()
    }

    fun openMap(map: Map) {
        if (canIInteractWithMinecraft()) {
            val file = File(app.cacheDir.absolutePath + "/" + map.id)
            openFile(file)
        }
    }

    fun openMod(mod: Mod) {
        if (canIInteractWithMinecraft()) {
            val file = File(app.cacheDir.absolutePath + "/" + mod.id)
            openFile(file)
        }
    }

    private fun openFile(file: File) {
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(app, app.packageName + ".provider", file)
            intent.setDataAndType(uri, "application/${file.absolutePath.split(".").last()}")
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            app.startActivity(intent)
        }
    }

    fun installSkin(skin: Skin): Boolean {
        if (canIInteractWithMinecraft()) {
            val file = File(app.cacheDir.absolutePath + "/" + skin.id)
            val copyTo =
                File(Environment.getExternalStorageDirectory().absolutePath + SKIN_LOCATION)
            if (copyTo.parentFile?.exists() != true) {
                copyTo.parentFile?.mkdirs()
            }
            if (!copyTo.exists()) {
                copyTo.createNewFile()
            }
            var source: FileChannel? = null
            var destination: FileChannel? = null
            return try {
                source = FileInputStream(file).channel
                destination = FileOutputStream(copyTo).channel
                destination.transferFrom(source, 0, source.size())
                true
            } catch (e: Exception) {
                false
            } finally {
                source?.close()
                destination?.close()
            }
        } else {
            return false
        }
    }

    fun downloadSkin(skin: Skin, actionAfter: () -> Unit) {
        CoroutineScope(IO).launch {
            if (mFirebaseRequests.loadFile(
                    skin.id,
                    File(app.cacheDir.absolutePath + "/" + skin.id)
                )
            ) {
                withContext(Main) {
                    actionAfter()
                }
            }
        }
    }

    fun downloadMod(mod: Mod, actionAfter: () -> Unit) {
        CoroutineScope(IO).launch {
            if (mFirebaseRequests.loadFile(
                    mod.id,
                    File(app.cacheDir.absolutePath + "/" + mod.id)
                )
            ) {
                withContext(Main) {
                    actionAfter()
                }
            }
        }
    }

    fun downloadMap(map: Map, actionAfter: () -> Unit) {
        CoroutineScope(IO).launch {
            if (mFirebaseRequests.loadFile(
                    map.id,
                    File(app.cacheDir.absolutePath + "/" + map.id)
                )
            ) {
                withContext(Main) {
                    actionAfter()
                }
            }
        }
    }

    private fun canIInteractWithMinecraft(): Boolean {
        return try {
            app.packageManager.getPackageInfo(MINECRAFT_PACKAGE_NAME, 0)
            true
        } catch (e: Exception) {
            try {
                val marketIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$MINECRAFT_PACKAGE_NAME")
                )
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK)
                app.startActivity(marketIntent)
            } catch (e: java.lang.Exception) {
                val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse(MINECRAFT_LINK))
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK)
                app.startActivity(marketIntent)
            }
            false
        }
    }

    fun checkIfSkinDownloaded(skin: Skin): Boolean {
        return File(app.cacheDir.absolutePath + "/" + skin.id).exists()
    }

    fun checkIfMapDownloaded(map: Map): Boolean {
        return File(app.cacheDir.absolutePath + "/" + map.id).exists()
    }

    fun checkIfModDownloaded(mod: Mod): Boolean {
        return File(app.cacheDir.absolutePath + "/" + mod.id).exists()
    }

    fun saveModsToDB(mods: List<Mod>) {
        mModsDao.insert(mods)
    }

    fun saveMapsToDB(mods: List<Map>) {
        mMapsDao.insert(mods)
    }

    fun saveSkinsToDB(mods: List<Skin>) {
        mSkinsDao.insert(mods)
    }

    fun saveDelModsToDB(mods: List<Mod>, category: String) {
        if (category == "") {
            mModsDao.deleteAll()
        } else {
            mModsDao.deleteCategory(category)
        }
        mModsDao.insert(mods)
    }

    fun saveDelMapsToDB(mods: List<Map>, category: String) {
        if (category == "") {
            mMapsDao.deleteAll()
        } else {
            mMapsDao.deleteCategory(category)
        }
        mMapsDao.insert(mods)
    }

    fun saveDelSkinsToDB(mods: List<Skin>, category: String) {
        if (category == "") {
            mSkinsDao.deleteAll()
        } else {
            mSkinsDao.deleteCategory(category)
        }
        mSkinsDao.insert(mods)
    }

    override fun onError(error: String) {

    }
}