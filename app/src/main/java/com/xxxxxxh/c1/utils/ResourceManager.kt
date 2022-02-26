package com.xxxxxxh.c1.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.xxxxxxh.c1.entity.ResourceEntity

class ResourceManager {
    companion object {
        private var instance: ResourceManager? = null
            get() {
                field?.let { } ?: run {
                    field = ResourceManager()
                }
                return field
            }

        @Synchronized
        fun get(): ResourceManager {
            return instance!!
        }
    }

    fun res2String(context: Context, id: Int): String {
        val r = context.resources
        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(id) + "/"
                    + r.getResourceTypeName(id) + "/"
                    + r.getResourceEntryName(id)
        )
        return uri.toString()
    }


    fun getResourceByFolder(
        context: Context,
        clazz: Class<*>,
        folderName: String,
        filter: String
    ): ArrayList<ResourceEntity> {
        val result = ArrayList<ResourceEntity>()
        for (field in clazz.fields) {
            val name = field.name
            if (name.startsWith(filter)) {
                val id = context.resources.getIdentifier(name, folderName, context.packageName)
                val entity = ResourceEntity(name, id)
                result.add(entity)
            }
        }
        return result
    }
}