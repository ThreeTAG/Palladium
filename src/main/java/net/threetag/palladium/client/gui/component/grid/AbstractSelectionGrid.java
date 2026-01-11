package net.threetag.palladium.client.gui.component.grid;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractSelectionGrid<E extends AbstractSelectionGrid.Entry<E>> extends AbstractContainerWidget {

    public static final Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    public static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");

    protected final Minecraft minecraft;
    protected final int itemHeight;
    protected final int itemWidth;
    protected final int gap;
    protected int itemPerRow;
    private final List<E> children;
    @Nullable
    private E selected;
    @Nullable
    private E hovered;

    public AbstractSelectionGrid(Minecraft minecraft, int x, int y, int width, int height, int itemHeight, int itemWidth, int gap) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.children = new TrackedList();
        this.minecraft = minecraft;
        this.itemHeight = itemHeight;
        this.itemWidth = itemWidth;
        this.gap = gap;
        this.itemPerRow = Mth.floor(this.width / (float) (itemWidth + gap));
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelectedIndex(int selected) {
        if (selected == -1) {
            this.setSelected(null);
        } else if (this.getItemCount() != 0) {
            this.setSelected(this.getEntry(selected));
        }
    }

    public void setSelected(@Nullable E selected) {
        this.selected = selected;
    }

    public E getFirstElement() {
        return (E) (this.children.getFirst());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public E getFocused() {
        return (E) super.getFocused();
    }

    @Override
    public final @NotNull List<E> children() {
        return this.children;
    }

    protected void clearEntries() {
        this.children.clear();
        this.selected = null;
    }

    public void replaceEntries(Collection<E> entries) {
        this.clearEntries();
        this.children.addAll(entries);
    }

    protected E getEntry(int index) {
        return this.children().get(index);
    }

    protected int addEntry(E entry) {
        this.children.add(entry);
        return this.children.size() - 1;
    }

    protected void addEntryToTop(E entry) {
        double d = (double) this.maxScrollAmount() - this.scrollAmount();
        this.children.addFirst(entry);
        this.setScrollAmount((double) this.maxScrollAmount() - d);
    }

    protected boolean removeEntryFromTop(E entry) {
        double d = (double) this.maxScrollAmount() - this.scrollAmount();
        boolean bl = this.removeEntry(entry);
        this.setScrollAmount((double) this.maxScrollAmount() - d);
        return bl;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    protected boolean isSelectedItem(int index) {
        return Objects.equals(this.getSelected(), this.children().get(index));
    }

    @Nullable
    protected final E getEntryAtPosition(double mouseX, double mouseY) {
        if (!this.isMouseOver(mouseX, mouseY) || this.getItemCount() == 0) {
            return null;
        }

        int halfRow = this.getRowWidth() / 2;
        int middle = this.getX() + (this.width / 2);
        int startRow = middle - halfRow;
        int endRow = middle + halfRow;
        int yPos = Mth.floor(mouseY - (double) this.getY()) + (int) this.scrollAmount();
        int row = yPos / (this.itemHeight + this.gap);
        int xPos = Mth.floor(mouseX - (double) this.getX() - (this.width - this.getRowWidth()) / 2F);
        int col = Mth.clamp(xPos / (this.itemWidth + this.gap), 0, this.itemPerRow - 1);
        int element = row * this.itemPerRow + col;
        return mouseX >= (double) startRow && mouseX <= (double) endRow && row >= 0 && yPos >= 0 && element < this.getItemCount() ? this.children().get(element) : null;
    }

    public void updateSize(ScreenRectangle rectangle) {
        this.updateSizeAndPosition(rectangle.width(), rectangle.height(), rectangle.left(), rectangle.top());
    }

    public void updateSizeAndPosition(int width, int height, int x, int y) {
        this.setSize(width, height);
        this.setPosition(x, y);
        this.refreshScrollAmount();
        this.itemPerRow = Mth.floor(this.width / (float) (itemWidth + gap));
    }

    @Override
    protected int contentHeight() {
        return this.getRowCount() * (this.itemHeight + this.gap);
    }

    protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
    }

    protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
        this.renderGridBackground(guiGraphics);
        this.enableScissor(guiGraphics);
        this.renderGridItems(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.disableScissor();
        this.renderGridSeparators(guiGraphics);
        this.renderScrollbar(guiGraphics, mouseX, mouseY);
        this.renderDecorations(guiGraphics, mouseX, mouseY);
    }

    protected void renderGridSeparators(GuiGraphics guiGraphics) {
        Identifier identifier = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        Identifier identifier2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier2, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }

    protected void renderGridBackground(GuiGraphics guiGraphics) {
        Identifier identifier = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                identifier,
                this.getX(),
                this.getY(),
                this.getRight(),
                this.getBottom() + (int) this.scrollAmount(),
                this.getWidth(),
                this.getHeight(),
                32,
                32
        );
    }

    protected void enableScissor(GuiGraphics guiGraphics) {
        guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
    }

    protected void centerScrollOn(E entry) {
        int row = Mth.floor(this.children().indexOf(entry) / (float) this.itemPerRow);
        int rowHeight = this.itemHeight + this.gap;
        this.setScrollAmount(row * rowHeight + rowHeight / 2D - this.height / 2D);
    }

    protected void ensureVisible(E entry) {
        int row = Mth.floor(this.children().indexOf(entry) / (float) this.itemPerRow);
        int i = this.getRowTop(row);
        int j = i - this.getY() - this.gap - this.itemHeight;
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.getBottom() - i - this.itemHeight - this.itemHeight;
        if (k < 0) {
            this.scroll(-k);
        }
    }

    private void scroll(int scroll) {
        this.setScrollAmount(this.scrollAmount() + (double) scroll);
    }

    @Override
    protected double scrollRate() {
        return (double) this.itemHeight / (double) 2.0F;
    }

    @Override
    public @NotNull Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return Optional.ofNullable(this.getEntryAtPosition(mouseX, mouseY));
    }

    public void setFocused(@Nullable GuiEventListener focused) {
        E e = this.getFocused();
        if (e != focused && e instanceof ContainerEventHandler containereventhandler) {
            containereventhandler.setFocused(null);
        }

        super.setFocused(focused);
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction) {
        return this.nextEntry(direction, (entry) -> true);
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction, Predicate<E> predicate) {
        return this.nextEntry(direction, predicate, this.getSelected());
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction, Predicate<E> predicate, @Nullable E selected) {
        int i = switch (direction) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
        };

        if (!this.children().isEmpty() && i != 0) {
            int j;
            if (selected == null) {
                j = i > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(selected) + i;
            }

            for (int k = j; k >= 0 && k < this.children.size(); k += i) {
                E entry = this.children().get(k);
                if (predicate.test(entry)) {
                    return entry;
                }
            }
        }

        return null;
    }

    protected void renderGridItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int rowLeft = this.getRowLeft();
        int rowCount = this.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            int rowTop = this.getRowTop(row);
            int rowBottom = this.getRowBottom(row);

            for (int col = 0; col < this.itemPerRow; col++) {
                int index = (row * this.itemPerRow) + col;

                if (rowBottom >= this.getY() && rowTop <= this.getBottom() && index < this.getItemCount()) {
                    this.renderItem(guiGraphics, mouseX, mouseY, partialTick, index,
                            rowLeft + ((this.itemWidth + this.gap) * col) + (this.gap / 2),
                            rowTop + (this.gap / 2), this.itemWidth, this.itemHeight);
                }
            }
        }
    }

    protected void renderItem(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        E entry = this.getEntry(index);
        entry.renderBack(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, entry), partialTick);
        if (this.isSelectedItem(index)) {
            int i = this.isFocused() ? -1 : -8355712;
            this.renderSelection(guiGraphics, top, left, width, height, i, -16777216);
        }

        entry.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, entry), partialTick);
    }

    protected void renderSelection(GuiGraphics guiGraphics, int top, int left, int width, int height, int outerColor, int innerColor) {
        guiGraphics.fill(left - 2, top - 2, left + width + 2, top + height + 2, outerColor);
        guiGraphics.fill(left - 1, top - 1, left + width + 1, top + height + 1, innerColor);
    }

    public int getRowCount() {
        return Mth.ceil(this.getItemCount() / (float) this.itemPerRow);
    }

    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2;
    }

    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    public int getRowTop(int index) {
        return this.getY() - (int) this.scrollAmount() + index * (this.itemHeight + this.gap);
    }

    public int getRowBottom(int index) {
        return this.getRowTop(index) + this.itemHeight + this.gap;
    }

    public int getRowWidth() {
        return this.itemPerRow * (this.itemWidth + this.gap);
    }

    public NarratableEntry.@NotNull NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.hovered != null ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Nullable
    protected E remove(int index) {
        E entry = this.children.get(index);
        return this.removeEntry(this.children.get(index)) ? entry : null;
    }

    protected boolean removeEntry(E entry) {
        boolean bl = this.children.remove(entry);
        if (bl && entry == this.getSelected()) {
            this.setSelected(null);
        }

        return bl;
    }

    @Nullable
    protected E getHovered() {
        return this.hovered;
    }

    void bindEntryToSelf(AbstractSelectionGrid.Entry<E> entry) {
        entry.grid = this;
    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput, E entry) {
        List<E> list = this.children();
        if (list.size() > 1) {
            int i = list.indexOf(entry);
            if (i != -1) {
                narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", new Object[]{i + 1, list.size()}));
            }
        }

    }

    public abstract static class Entry<E extends AbstractSelectionGrid.Entry<E>> implements GuiEventListener {

        AbstractSelectionGrid<E> grid;

        protected Entry() {
        }

        public void setFocused(boolean focused) {
        }

        public boolean isFocused() {
            return this.grid.getFocused() == this;
        }

        public abstract void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick);

        public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
        }

        public boolean isMouseOver(double mouseX, double mouseY) {
            return Objects.equals(this.grid.getEntryAtPosition(mouseX, mouseY), this);
        }
    }

    class TrackedList extends AbstractList<E> {
        private final List<E> delegate = Lists.newArrayList();

        public E get(int index) {
            return this.delegate.get(index);
        }

        public int size() {
            return this.delegate.size();
        }

        public E set(int index, E entry) {
            E entry2 = this.delegate.set(index, entry);
            AbstractSelectionGrid.this.bindEntryToSelf(entry);
            return entry2;
        }

        public void add(int index, E entry) {
            this.delegate.add(index, entry);
            AbstractSelectionGrid.this.bindEntryToSelf(entry);
        }

        public E remove(int index) {
            return this.delegate.remove(index);
        }
    }

}
