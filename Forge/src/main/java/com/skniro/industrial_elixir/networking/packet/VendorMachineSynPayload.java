package com.skniro.industrial_elixir.networking.packet;

import com.skniro.industrial_elixir.IndustrialElixir;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.List;
import java.util.Optional;

public record VendorMachineSynPayload(List<OfferData> offers) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, "vendor_machine_sync");
    public static final Type<VendorMachineSynPayload> TYPE = new Type<>(ID);

    public record OfferData(ItemStack costA, Optional<ItemStack> costB, ItemStack result,
                            int maxUses, int xp, float priceMultiplier) {
        public static final StreamCodec<RegistryFriendlyByteBuf, OfferData> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, OfferData::costA,
                ByteBufCodecs.optional(ItemStack.STREAM_CODEC), OfferData::costB,
                ItemStack.STREAM_CODEC, OfferData::result,
                ByteBufCodecs.INT, OfferData::maxUses,
                ByteBufCodecs.INT, OfferData::xp,
                ByteBufCodecs.FLOAT, OfferData::priceMultiplier,
                OfferData::new
        );

        public MerchantOffer toOffer() {
            return new MerchantOffer(
                    new ItemCost(costA.getItem(), costA.getCount()),
                    costB.map(s -> new ItemCost(s.getItem(), s.getCount())),
                    result,
                    maxUses, xp, priceMultiplier
            );
        }

        public static OfferData fromOffer(MerchantOffer o) {
            return new OfferData(
                    o.getItemCostA().itemStack(),
                    o.getItemCostB().map(ItemCost::itemStack),
                    o.getResult(),
                    o.getMaxUses(), o.getXp(), o.getPriceMultiplier()
            );
        }
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, VendorMachineSynPayload> CODEC =
            StreamCodec.composite(
                    OfferData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    VendorMachineSynPayload::offers,
                    VendorMachineSynPayload::new
            );

    public static VendorMachineSynPayload fromMerchantOffers(MerchantOffers offers) {
        return new VendorMachineSynPayload(
                offers.stream().map(OfferData::fromOffer).toList()
        );
    }

    public MerchantOffers toMerchantOffers() {
        MerchantOffers result = new MerchantOffers();
        offers.forEach(o -> result.add(o.toOffer()));
        return result;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
