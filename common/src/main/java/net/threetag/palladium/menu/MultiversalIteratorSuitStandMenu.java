package net.threetag.palladium.menu;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.multiverse.MultiversalItemVariantsManager;
import net.threetag.palladium.network.MultiverseIteratorSuitStandConfirmMessage;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncMultiversalIteratorSuitPagesMessage;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import net.threetag.palladium.util.PlayerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MultiversalIteratorSuitStandMenu extends AbstractContainerMenu {

    private final DataSlot resultCount = DataSlot.standalone();
    private final SimpleContainer extrapolatorSlotContainer = createContainer(1);
    protected final ContainerLevelAccess access;
    protected final Player player;
    public ArmorStand suitStand;
    protected List<IterationPage> pages;
    protected int selectedIndex = 0;
    protected IterationPage selectedPage;

    public MultiversalIteratorSuitStandMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, (ArmorStand) playerInventory.player.level().getEntity(buf.readInt()), ContainerLevelAccess.NULL);
    }

    public MultiversalIteratorSuitStandMenu(int containerId, Inventory playerInventory, ArmorStand suitStand, ContainerLevelAccess access) {
        super(PalladiumMenuTypes.MULTIVERSAL_ITERATOR_SUIT_STAND.get(), containerId);
        this.addDataSlot(this.resultCount);
        this.player = playerInventory.player;
        this.access = access;
        this.suitStand = suitStand;

        this.addSlot(new Slot(this.extrapolatorSlotContainer, 0, 38, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof MultiversalExtrapolatorItem;
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 132 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 190));
        }

        this.setPages(createPages(this.suitStand, this.extrapolatorSlotContainer.getItem(0)));
    }

    public void setPages(List<IterationPage> pages) {
        this.pages = pages;
        this.selectedIndex = 0;
        this.selectedPage = this.pages.get(0);

        if (this.player instanceof ServerPlayer serverPlayer) {
            PalladiumNetwork.NETWORK.sendToPlayer(serverPlayer, new SyncMultiversalIteratorSuitPagesMessage(this.pages));
        }
    }

    public List<IterationPage> getPages() {
        return ImmutableList.copyOf(this.pages);
    }

    public IterationPage getSelectedPage() {
        return selectedPage;
    }

    public void switchPage(boolean forward) {
        if (forward) {
            this.selectedIndex = this.selectedIndex >= this.pages.size() - 1 ? 0 : this.selectedIndex + 1;
        } else {
            this.selectedIndex = this.selectedIndex <= 0 ? this.pages.size() - 1 : this.selectedIndex - 1;
        }
        this.selectedPage = this.pages.get(this.selectedIndex);
    }

    public void convert(int pageId) {
        if (this.player instanceof ServerPlayer serverPlayer) {
            var page = this.pages.stream().filter(p -> p.id() == pageId).findFirst().orElseThrow();
            this.damageExtrapolator(page.set(this.suitStand));
            this.access.execute((level, pos) -> PlayerUtil.playSoundToAll(player.level(), player.getX(), player.getEyeY(), player.getZ(), 50, PalladiumSoundEvents.MULTIVERSAL_VARIANT_CRAFTED.get(), SoundSource.PLAYERS));
        } else {
            PalladiumNetwork.NETWORK.sendToServer(new MultiverseIteratorSuitStandConfirmMessage(pageId));
        }
    }

    private void damageExtrapolator(int amount) {
        ItemStack itemStack = this.extrapolatorSlotContainer.getItem(0);
        if (!itemStack.isEmpty()) {
            itemStack.hurtAndBreak(amount, this.player, player1 -> this.extrapolatorSlotContainer.setItem(0, ItemStack.EMPTY));
            this.extrapolatorSlotContainer.setItem(0, itemStack);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.setPages(createPages(this.suitStand, this.extrapolatorSlotContainer.getItem(0)));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.suitStand.isAlive() && this.access
                .evaluate(
                        (level, blockPos) -> level.getBlockState(blockPos).is(PalladiumBlocks.MULTIVERSAL_ITERATOR.get()) && player.distanceToSqr((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5) <= 64.0,
                        true
                );
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.extrapolatorSlotContainer));
    }

    private SimpleContainer createContainer(int size) {
        return new SimpleContainer(size) {
            @Override
            public void setChanged() {
                super.setChanged();
                MultiversalIteratorSuitStandMenu.this.slotsChanged(this);
            }
        };
    }

    public static List<IterationPage> createPages(ArmorStand armorStand, ItemStack extrapolator) {
        List<IterationPage> pages = new ArrayList<>();
        pages.add(new IterationPage(
                0,
                armorStand.getItemBySlot(EquipmentSlot.HEAD),
                armorStand.getItemBySlot(EquipmentSlot.CHEST),
                armorStand.getItemBySlot(EquipmentSlot.LEGS),
                armorStand.getItemBySlot(EquipmentSlot.FEET),
                armorStand.getItemBySlot(EquipmentSlot.MAINHAND),
                armorStand.getItemBySlot(EquipmentSlot.OFFHAND)
        ));

        if (!extrapolator.isEmpty()) {
            var universe = MultiversalExtrapolatorItem.getUniverse(extrapolator, armorStand.level());

            if (universe != null) {
                int id = 1;

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    var originalItem = armorStand.getItemBySlot(slot);

                    for (Item item : MultiversalItemVariantsManager.getInstance(armorStand.level()).getVariantsOf(originalItem.getItem(), universe)) {
                        boolean found = false;

                        for (int i = 1; i < pages.size(); i++) {
                            if (pages.get(i).tryAdd(item, slot)) {
                                found = true;
                            }
                        }

                        if (!found) {
                            pages.add(new IterationPage(id++, slot, item));
                        }
                    }
                }
            }
        }

        return pages;
    }

    public static final class IterationPage {

        private final int id;
        private final Map<EquipmentSlot, ItemStack> items;

        public IterationPage(int id, EquipmentSlot slot, Item item) {
            this.id = id;
            this.items = new HashMap<>();
            this.items.put(slot, item.getDefaultInstance());
        }

        public IterationPage(int id, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
                             ItemStack mainhand, ItemStack offhand) {
            this.id = id;
            this.items = new HashMap<>();
            this.items.put(EquipmentSlot.HEAD, helmet);
            this.items.put(EquipmentSlot.CHEST, chestplate);
            this.items.put(EquipmentSlot.LEGS, leggings);
            this.items.put(EquipmentSlot.FEET, boots);
            this.items.put(EquipmentSlot.MAINHAND, mainhand);
            this.items.put(EquipmentSlot.OFFHAND, offhand);
        }

        public IterationPage(FriendlyByteBuf buf) {
            this.id = buf.readInt();
            this.items = buf.readMap(b -> EquipmentSlot.byName(buf.readUtf()), b -> BuiltInRegistries.ITEM.get(buf.readResourceLocation()).getDefaultInstance());
        }

        public void toNetwork(FriendlyByteBuf buf) {
            buf.writeInt(this.id);
            buf.writeMap(this.items, (b, s) -> b.writeUtf(s.getName()), (b, s) -> b.writeResourceLocation(BuiltInRegistries.ITEM.getKey(s.getItem())));
        }

        public int id() {
            return this.id;
        }

        public ItemStack helmet() {
            return this.items.getOrDefault(EquipmentSlot.HEAD, ItemStack.EMPTY);
        }

        public ItemStack chestplate() {
            return this.items.getOrDefault(EquipmentSlot.CHEST, ItemStack.EMPTY);
        }

        public ItemStack leggings() {
            return this.items.getOrDefault(EquipmentSlot.LEGS, ItemStack.EMPTY);
        }

        public ItemStack boots() {
            return this.items.getOrDefault(EquipmentSlot.FEET, ItemStack.EMPTY);
        }

        public ItemStack mainhand() {
            return this.items.getOrDefault(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        public ItemStack offhand() {
            return this.items.getOrDefault(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        public boolean tryAdd(Item item, EquipmentSlot slot) {
            if (this.items.isEmpty()) {
                this.items.put(slot, item.getDefaultInstance());
                return true;
            }

            if (this.items.containsKey(slot)) {
                return false;
            }

            var anyItem = this.items.values().stream().findAny().orElseThrow();
            var suitSet1 = SuitSet.ITEM_SUIT_SET_LOOK_UP.get(item);
            var suitSet2 = SuitSet.ITEM_SUIT_SET_LOOK_UP.get(anyItem);

            if (suitSet2 != null && suitSet1 == suitSet2) {
                this.items.put(slot, item.getDefaultInstance());
                return true;
            }

            if (item instanceof ArmorItem a1 && anyItem.getItem() instanceof ArmorItem a2 && a1.getMaterial() == a2.getMaterial()) {
                this.items.put(slot, item.getDefaultInstance());
                return true;
            }

            return false;
        }

        public int set(ArmorStand armorStand) {
            int i = 0;

            for (EquipmentSlot slot : this.items.keySet()) {
                armorStand.setItemSlot(slot, this.items.get(slot).copy());
                i++;
            }

            return i;
        }

        public void pretend(ArmorStand armorStand, Consumer<ArmorStand> consumer) {
            Map<EquipmentSlot, ItemStack> cached = new HashMap<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                cached.put(slot, armorStand.getItemBySlot(slot));
                armorStand.setItemSlot(slot, this.items.getOrDefault(slot, ItemStack.EMPTY));
            }
            consumer.accept(armorStand);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                armorStand.setItemSlot(slot, cached.getOrDefault(slot, ItemStack.EMPTY));
            }
        }

    }
}
