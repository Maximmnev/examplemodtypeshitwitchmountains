package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Random;

import static com.example.examplemod.ExampleMod20.AMAFUNNYENCHANTMENT;
import static com.example.examplemod.ExampleMod20.CROSSBOWING;

@EventBusSubscriber(modid = "examplemod20")
public class EnchantmentParticleHandler {
    public static Projectile createCopy(Projectile projectile ,LivingEntity shooter ,Level level){
        Projectile extraShot = (Projectile) projectile.getType().create(level, EntitySpawnReason.COMMAND);

        if (extraShot != null) {
            // 2. Match the original projectile's exact position
            extraShot.setPos(projectile.getX(), projectile.getY(), projectile.getZ());

            // 3. Set the owner so the game knows who fired it
            extraShot.setOwner(shooter);

            // 4. Mark as an extra shot using NBT to prevent infinite loops
            extraShot.getPersistentData().putBoolean("is_extra_shot", true);

            // 5. Adjust trajectory for the spread effect
            extraShot.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 3.15F, 1.0F);

            // 6. Use addFreshEntity to spawn it into the world
//            level.addFreshEntity(extraShot);
        }


        return extraShot;
    };


    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        // In AttackEntityEvent, getEntity() is the PLAYER attacking
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level();

        if (level.isClientSide()) return;

        ItemStack stack = player.getMainHandItem();
        var enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = enchantmentRegistry.getOrThrow(AMAFUNNYENCHANTMENT);

        // Check for enchantment using the Holder system
        // Replace YOUR_ENCHANTMENT_HOLDER with your actual enchantment registry object
        if (stack.getEnchantmentLevel(holder) > 0) {
            ServerLevel serverLevel = (ServerLevel) level;

            double centerX = target.getX();
            double centerY = target.getY();
            double centerZ = target.getZ();

            // Spiral of dark wither particles (using WITCH for dark purple/black)
            double radius = 0.5;
            for (double i = 0; i < 2.5; i += 0.1) {
                double angle = i * 7; // Rotation speed
                double xOff = Math.cos(angle) * radius;
                double zOff = Math.sin(angle) * radius;
                double yOff = i; // Upward movement

                serverLevel.sendParticles(
                        ParticleTypes.WITCH,
                        centerX + xOff,
                        centerY + yOff,
                        centerZ + zOff,
                        1, 0, 0, 0, 0.0
                );
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level();


        // Ensure this only runs on the server side
        if (!level.isClientSide()) {
            // Check if the player has a custom tag to identify if they've joined before
            if (!player.entityTags().contains("has_spawned_platform")) {
                java.util.Random notfun = new Random();
                int notthatfun =notfun.nextInt(0,21);
                int notthatun =notfun.nextInt(30,41);
                int notthatfn =notfun.nextInt(43,100);

                BlockPos playerPos = player.blockPosition();
                var one = new PlacingABasicPlatformIGuess(playerPos.offset(0,notthatfun,0),"low",level);
                var two = new PlacingABasicPlatformIGuess(playerPos.offset(0,notthatun,0),"somewhere",level);
                var numbertree = new PlacingABasicPlatformIGuess(playerPos.offset(0,notthatfn,0),"high",level);


                double newY = player.getY() + notthatfn+2;
                player.teleportTo(player.getX(), newY, player.getZ());

                player.addTag("has_spawned_platform");
            }
        }
    }
    @SubscribeEvent
    public static void onProjectileSpawn(EntityJoinLevelEvent event) {
        var level = event.getLevel();
        var enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = enchantmentRegistry.getOrThrow(CROSSBOWING);
        // Only proceed on the server side and for projectile entities
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Projectile projectile)) {


            return;

        }

        // Check if the owner is a living entity (like a player)
        if (projectile.getOwner() instanceof LivingEntity shooter) {
            ItemStack weapon = shooter.getMainHandItem();

            // Ensure the weapon is a crossbow and has your custom enchantment
            if (weapon.getItem() instanceof CrossbowItem &&
                    weapon.getEnchantmentLevel(holder) > 0) {

                // Logic to prevent an infinite loop of spawning copies
                if (projectile.getPersistentData().getBoolean("is_extra_shot").orElseGet(()->false)) return;

                // Spawn 2 additional shots for a total of 3
                for (int i = 0; i < 2; i++) {
                    Projectile extraShot = createCopy(projectile, shooter,level);
                    extraShot.getPersistentData().putBoolean("is_extra_shot", true);

                    // Adjust trajectory slightly for a "spread"
                    extraShot.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(),
                            0.0F, 3.15F, 1.0F);

                    event.getLevel().addFreshEntity(extraShot);
                }
            }
        }
    }














}