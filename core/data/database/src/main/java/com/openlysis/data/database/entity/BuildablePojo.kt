package com.openlysis.data.database.entity

/**
 * Defines a POJO that can be built into a model.
 *
 * @param TModel The type of the model to build.
 */
internal interface BuildablePojo<TModel> where TModel : Any {
    /**
     * Builds and returns the model object.
     *
     * @return The built model of type [TModel].
     */
    fun buildModel(): TModel
}