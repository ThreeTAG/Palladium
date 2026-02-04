package net.threetag.palladium.client.gui.component;

import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.LinkedList;
import java.util.List;

public class PowerTreePopulator {

    public static void generateTree(PowerInstance powerInstance, List<PowerTreeWidget.AbilityElement> abilities) {
        List<PowerTreeWidget.AbilityElement> root = new LinkedList<>();

        // Create entry for each ability
        for (AbilityInstance<?> ability : powerInstance.getAbilities().values()) {
            if (!ability.getAbility().getProperties().isHiddenInGUI()) {
                var widget = new PowerTreeWidget.AbilityElement(ability, ability.getAbility().getDisplayName(), ability.getAbility().getProperties().getIcon()).setGridPos(0, 0);
                abilities.add(widget);

                var pos = ability.getAbility().getProperties().getGuiPosition();
                if (pos != null) {
                    widget.setGridPosFixed(pos.x, pos.y);
                }
            }
        }

        // Find parents and children for each
        for (PowerTreeWidget.AbilityElement entry : abilities) {
            entry.parents.clear();
            entry.children.clear();
        }
        for (PowerTreeWidget.AbilityElement entry : abilities) {
            entry.updateRelatives(abilities);
        }

        // Locate and set first row
        int y = 0;
        for (PowerTreeWidget.AbilityElement entry : abilities) {
            if (entry.parents.isEmpty()) {
                if (!entry.fixedPosition) {
                    entry.updatePosition(0, getFreeGridYPos(abilities, 0, y));
                    y++;
                }
                root.add(entry);
            }
        }

        int longest = getLongestGridColumnLength(abilities);

        // Set position for children
        for (int j = 0; j < root.size(); j++) {
            for (PowerTreeWidget.AbilityElement entry : root) {
                for (PowerTreeWidget.AbilityElement child : entry.children) {
                    if (!child.fixedPosition && entry.gridX == child.gridX) {
                        child.setGridPos(child.gridX + 1, getFreeGridYPos(abilities, child.gridX + 1, entry.gridY));
                    }
                }
            }
        }

        // Last Adjustments
        for (int x = 0; x < 100; x++) {
            List<PowerTreeWidget.AbilityElement> entries = getEntriesAtX(abilities, x);
            for (int n = 0; n < entries.size(); n++) {
                PowerTreeWidget.AbilityElement entry = entries.get(n);
                if (!entry.fixedPosition) {
                    entry.setGridPos(entry.gridX, (longest / 2F) - (entries.size() / 2F) + n);
                }
            }
        }

//        // Fixing min & max size; make lines
//        for (PowerTreeWidget.AbilityElement entry : abilities) {
//            this.minX = (int) Math.min((entry.gridX - 1) * GRID_SIZE, this.minX);
//            this.minY = (int) Math.min((entry.gridY - 1) * GRID_SIZE, this.minY);
//            this.maxX = (int) Math.max((entry.gridX + 1) * GRID_SIZE, this.maxX);
//            this.maxY = (int) Math.max((entry.gridY + 1) * GRID_SIZE, this.maxY);
//
//            for (TreeAbilityWidget child : entry.children) {
//                TreePowerTab.Connection connection = new TreePowerTab.Connection();
//                int startX = toCoord(entry.gridX);
//                int startY = toCoord(entry.gridY, 1D / (entry.children.size() + 1) * (entry.children.indexOf(child) + 1));
//                int endX = toCoord(child.gridX);
//                int endY = toCoord(child.gridY, 1D / (child.parents.size() + 1) * (child.parents.indexOf(entry) + 1));
//                connection.addLine(new TreePowerTab.ConnectionLine(startX, startY, endX, startY));
//                connection.addLine(new TreePowerTab.ConnectionLine(endX, startY, endX, endY));
//                connection.color = child.abilityInstance.isUnlocked() ? this.powerInstance.getPower().value().getPrimaryColor() : this.powerInstance.getPower().value().getSecondaryColor();
//                this.connections.add(connection);
//            }
//        }
    }

    public static void center(List<PowerTreeWidget.AbilityElement> abilities, int width, int height) {
        float gridWidth = getHighestXGridValue(abilities) * PowerTreeWidget.GRID_SIZE;
        float gridHeight = getHighestYGridValue(abilities) * PowerTreeWidget.GRID_SIZE;
        float xOffset = 0;
        float yOffset = 0;

        if (gridWidth < width) {
            xOffset = ((width - gridWidth) / PowerTreeWidget.GRID_SIZE) / 2F - 0.5F;
        }

        if (gridHeight < height) {
            yOffset = ((height - gridHeight) / PowerTreeWidget.GRID_SIZE) / 2F - 0.5F;
        }

        for (PowerTreeWidget.AbilityElement ability : abilities) {
            ability.gridX += xOffset;
            ability.gridY += yOffset;
        }
    }

    public static float getFreeGridYPos(List<PowerTreeWidget.AbilityElement> abilities, float x, float y) {
        for (int i = (int) y; i < 100; i++) {
            if (getEntryInGrid(abilities, x, i) == null) {
                return i;
            }
        }

        return 0;
    }

    public static PowerTreeWidget.AbilityElement getEntryInGrid(List<PowerTreeWidget.AbilityElement> abilities, float x, float y) {
        for (PowerTreeWidget.AbilityElement entry : abilities) {
            if (entry.gridX == x && entry.gridY == y) {
                return entry;
            }
        }

        return null;
    }

    public static int getLongestGridColumnLength(List<PowerTreeWidget.AbilityElement> abilities) {
        int l = 0;
        for (int i = 0; i < 100; i++) {
            int k = getEntriesAtX(abilities, i).size();
            l = Math.max(l, k);
        }
        return l;
    }

    public static List<PowerTreeWidget.AbilityElement> getEntriesAtX(List<PowerTreeWidget.AbilityElement> abilities, double x) {
        List<PowerTreeWidget.AbilityElement> list = new LinkedList<>();
        for (PowerTreeWidget.AbilityElement entry : abilities) {
            if (entry.gridX == x) {
                list.add(entry);
            }
        }

        return list;
    }

    public static float getHighestXGridValue(List<PowerTreeWidget.AbilityElement> abilities) {
        float max = 0;

        for (PowerTreeWidget.AbilityElement ability : abilities) {
            max = Math.max(max, ability.gridX);
        }

        return max;
    }

    public static float getHighestYGridValue(List<PowerTreeWidget.AbilityElement> abilities) {
        float max = 0;

        for (PowerTreeWidget.AbilityElement ability : abilities) {
            max = Math.max(max, ability.gridY);
        }

        return max;
    }

}
