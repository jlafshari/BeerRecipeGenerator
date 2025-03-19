package com.jlafshari.beerrecipegenerator.homebrewApi

import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipecore.batches.BatchUpdateInfo
import com.jlafshari.beerrecipecore.batches.NewBatchInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.BuildConfig
import com.jlafshari.beerrecipegenerator.RecipeSearchFilter
import com.jlafshari.beerrecipegenerator.settings.RecipeDefaultSettings
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

class HomebrewApiService @Inject constructor() {
    private var api: HomebrewApi

    init {
        val baseUrl = getBaseUrl()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(HomebrewApi::class.java)
    }

    private fun getBaseUrl(): String =
        "${BuildConfig.homebrewApiHttpScheme}://${BuildConfig.homebrewApiBaseDomain}"

    fun getAllRecipePreviews(
        authHeader: String, recipeSearchFilter: RecipeSearchFilter
    ) : Single<List<RecipePreview>> {
        with(recipeSearchFilter) {
            return api.getAllRecipePreviews(
                authHeader,
                abvMin,
                abvMax,
                colorMin?.toString(),
                colorMax?.toString(),
                recipeType,
                recipeNameSearchTerm,
                fermentables.map { it.id },
                daysSinceLastUpdated,
                hops.map { it.id },
            )
        }
    }

    fun getRecipeDetails(authHeader: String, recipeId: String): Single<Recipe> =
        api.getRecipeDetails(authHeader, recipeId)

    fun generateRecipe(authHeader: String, recipeGenerationInfo: RecipeGenerationInfo): Single<Recipe> =
        api.generateRecipe(authHeader, recipeGenerationInfo)

    fun updateRecipe(authHeader: String, recipeId: String, recipeUpdateInfo: RecipeUpdateInfo): Completable =
        api.updateRecipe(authHeader, recipeId, recipeUpdateInfo)

    fun deleteRecipe(authHeader: String, recipeId: String): Completable =
        api.deleteRecipe(authHeader, recipeId)

    fun getHopDetails(authHeader: String, hopId: String): Single<Hop> = api.getHopDetails(authHeader, hopId)

    fun getFermentableDetails(authHeader: String, fermentableId: String): Single<Fermentable> =
        api.getFermentableDetails(authHeader, fermentableId)

    fun getAllFermentables(authHeader: String): Single<List<Fermentable>> = api.getAllFermentables(authHeader)

    fun getAllHops(authHeader: String): Single<List<Hop>> = api.getAllHops(authHeader)

    fun getAllStyles(authHeader: String): Single<List<Style>> = api.getAllStyles(authHeader)

    fun getRecipeDefaultSettings(authHeader: String): Single<RecipeDefaultSettings> =
        api.getRecipeDefaultSettings(authHeader)

    fun getBatchDetails(authHeader: String, batchId: String) =
        api.getBatchDetails(authHeader, batchId)

    fun updateBatch(authHeader: String, batchId: String, batchUpdateInfo: BatchUpdateInfo): Completable =
        api.updateBatch(authHeader, batchId, batchUpdateInfo)

    fun getCorrectedRefractometerReading(
        authHeader: String, originalGravity: Double, finalGravity: Double
    ) : Single<Double> = api.getCorrectedRefractometerReading(authHeader, originalGravity, finalGravity)

    fun newBatch(authHeader: String, newBatchInfo: NewBatchInfo): Single<String> =
        api.newBatch(authHeader, newBatchInfo)
}