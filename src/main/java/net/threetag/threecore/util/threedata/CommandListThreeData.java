package net.threetag.threecore.util.threedata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.LinkedList;
import java.util.List;

public class CommandListThreeData extends ThreeData<CommandListThreeData.CommandList> {

    public CommandListThreeData(String key) {
        super(key);
    }

    @Override
    public CommandList parseValue(JsonObject jsonObject, CommandList defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return new CommandList(jsonObject.get(this.jsonKey));

    }

    @Override
    public void writeToNBT(CompoundNBT nbt, CommandList value) {
        nbt.put(this.key, value.serializeNBT());
    }

    @Override
    public CommandList readFromNBT(CompoundNBT nbt, CommandList defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        CommandList list = new CommandList();
        list.deserializeNBT(nbt.getList(this.key, Constants.NBT.TAG_STRING));
        return list;
    }

    @Override
    public JsonElement serializeJson(CommandList value) {
        return value.serializeJson();
    }

    public static class CommandList implements INBTSerializable<ListNBT> {

        private List<String> commands;

        public CommandList() {
            this(new LinkedList<>());
        }

        public CommandList(JsonElement jsonElement) {
            this.parseJson(jsonElement);
        }

        public CommandList(List<String> commands) {
            this.commands = commands;
        }

        public CommandList addCommand(String command) {
            this.commands.add(command);
            return this;
        }

        public List<String> getCommands() {
            return commands;
        }

        @Override
        public ListNBT serializeNBT() {
            ListNBT list = new ListNBT();
            for (int i = 0; i < this.commands.size(); i++) {
                list.add(i, StringNBT.valueOf(this.commands.get(i)));
            }
            return list;
        }

        @Override
        public void deserializeNBT(ListNBT nbt) {
            this.commands = new LinkedList<>();
            for (int i = 0; i < nbt.size(); i++) {
                this.commands.add(nbt.getString(i));
            }
        }

        public void parseJson(JsonElement jsonElement) {
            this.commands = new LinkedList<>();

            if (jsonElement.isJsonPrimitive()) {
                this.commands.add(jsonElement.getAsString());
            } else {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    this.commands.add(jsonArray.get(i).getAsString());
                }
            }
        }

        public JsonElement serializeJson() {
            if (this.commands.size() == 1) {
                return new JsonPrimitive(this.commands.get(0));
            } else {
                JsonArray array = new JsonArray();
                for (String s : this.commands)
                    array.add(s);
                return array;
            }
        }

    }


}
