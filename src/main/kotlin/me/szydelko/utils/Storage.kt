package me.szydelko.utils

data class StorageEntity(val id: String?, var value: Any);

data class Storage(var listEntities: MutableList<StorageEntity> = mutableListOf()) {
    inline fun <reified T> get(id: String? = null): T? {
        return listEntities.firstOrNull { it.id == id && it.value is T }?.value as? T
    }

    inline fun <reified T : Any> push(value: T, id: String?=null): Storage {
        listEntities.firstOrNull { it.id == id && it.value is T}?.apply { this.value = value } ?: listEntities.add(StorageEntity(id,value))
        return this
    }
}

