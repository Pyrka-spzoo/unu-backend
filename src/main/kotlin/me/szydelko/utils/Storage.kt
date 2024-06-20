package me.szydelko.utils

data class StorageEntity(val id: String?, var value: Any);
data class StorageEntityType<T>(val id: String?, var value: T);

data class Storage(var listEntities: MutableList<StorageEntity> = mutableListOf()) {
    inline fun <reified T> get(id: String? = null): T? {
        return listEntities.firstOrNull { it.id == id && it.value is T }?.value as? T
    }

    inline fun <reified T : Any> push(value: T, id: String?=null): Storage {
        listEntities.firstOrNull { it.id == id && it.value is T}?.apply { this.value = value } ?: listEntities.add(StorageEntity(id,value))
        return this
    }

    inline fun <reified T : Any> remove(id: String? = null): Boolean {
        return listEntities.removeIf { it.id == id && it.value is T }
    }

    inline fun <reified T : Any> getAll(regex: Regex? = null): List<T> {
        if (regex == null) {
            return listEntities.filter(){ it.value is T }.map { it.value as T }
        }
        return listEntities.filter(){ it.value is T && regex matches (it.id ?: "") }.map { it.value as T }
    }

    inline fun <reified T : Any> getAllWithId(regex: Regex? = null): List<StorageEntityType<T>> {
        if (regex == null) {
            return listEntities.filter(){ it.value is T }.map { StorageEntityType<T>(it.id,it.value as T) }
        }
        return listEntities.filter(){ it.value is T && regex matches (it.id ?: "") }.map { StorageEntityType<T>(it.id,it.value as T) }
    }

    inline fun <reified T : Any> removeAll(regex: Regex? = null): Boolean{
        if (regex == null) {
            return listEntities.removeIf() { it.value is T }
        }
        return listEntities.removeIf(){ it.value is T && regex matches (it.id ?: "") }
    }



}

