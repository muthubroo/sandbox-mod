package com.muthubroo.sandboxmod.datagen;

import com.muthubroo.sandboxmod.SandboxMod;
import com.muthubroo.sandboxmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS)
                        .input('C', Items.COPPER_INGOT)
                        .pattern("C C")
                        .pattern("C C")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE)
                        .input('C', Items.COPPER_INGOT)
                        .pattern("C C")
                        .pattern("CCC")
                        .pattern("CCC")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET)
                        .input('C', Items.COPPER_INGOT)
                        .pattern("CCC")
                        .pattern("C C")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS)
                        .input('C', Items.COPPER_INGOT)
                        .pattern("CCC")
                        .pattern("C C")
                        .pattern("C C")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes for " + SandboxMod.MOD_ID;
    }
}
