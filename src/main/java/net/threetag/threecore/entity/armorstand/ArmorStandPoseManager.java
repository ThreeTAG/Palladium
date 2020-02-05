package net.threetag.threecore.entity.armorstand;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.Rotations;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.TCJsonUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ArmorStandPoseManager {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final File DIRECTORY = new File("config/" + ThreeCore.MODID + "/armorstandposes");
    public static Map<String, ArmorStandPose> POSES = Maps.newHashMap();

    public static int init() {
        DIRECTORY.mkdirs();
        POSES.clear();

        for (File file : FileUtils.listFiles(DIRECTORY, new String[]{"json"}, true)) {
            try {
                InputStream inputStream = new FileInputStream(file);
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                JsonObject json = JSONUtils.fromJson(GSON, reader, JsonObject.class);
                String name = FilenameUtils.getBaseName(file.getName());
                try {
                    ArmorStandPose pose = parse(json);
                    register(name, pose);
                } catch (Exception e) {
                    ThreeCore.LOGGER.error("Parsing error loading armor stand pose {}", name, e);
                }

                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        ThreeCore.LOGGER.info("Loaded {} armor stand poses from files", POSES.size());

        if (POSES.size() == 0) {
            ThreeCore.LOGGER.info("Generating default armor stand poses...");

            // Dab
            generateFile("dab", new ArmorStandPose(new Rotations(45, 0, 0), ArmorStandPose.EMPTY_ROTATION, new Rotations(90, 310, 15), new Rotations(90, 220, 0), ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION));

            // Zombie
            generateFile("zombie", new ArmorStandPose(ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION, new Rotations(270, 0, 0), new Rotations(270, 0, 0), ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION));

            // T-Pose
            generateFile("t-pose", new ArmorStandPose(ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION, new Rotations(0, 0, 90), new Rotations(0, 0, 270), ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION));

            // Cheering
            generateFile("cheering", new ArmorStandPose(ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION, new Rotations(0, 0, 150), new Rotations(0, 0, 210), ArmorStandPose.EMPTY_ROTATION, ArmorStandPose.EMPTY_ROTATION));
        }

        return POSES.size();
    }

    private static void generateFile(String name, ArmorStandPose pose) {
        try (FileWriter w = new FileWriter(new File(DIRECTORY, name + ".json"))) {
            GSON.toJson(pose.serialize(), w);
        } catch (IOException e) {
            e.printStackTrace();
        }

        register(name, pose);
    }

    public static ArmorStandPose parse(JsonObject json) {
        Rotations head = makeRotations(TCJsonUtil.getFloatArray(json, 3, "head", 0, 0, 0));
        Rotations body = makeRotations(TCJsonUtil.getFloatArray(json, 3, "body", 0, 0, 0));
        Rotations rightArm = makeRotations(TCJsonUtil.getFloatArray(json, 3, "right_arm", 0, 0, 0));
        Rotations leftArm = makeRotations(TCJsonUtil.getFloatArray(json, 3, "left_arm", 0, 0, 0));
        Rotations rightLeg = makeRotations(TCJsonUtil.getFloatArray(json, 3, "right_leg", 0, 0, 0));
        Rotations leftLeg = makeRotations(TCJsonUtil.getFloatArray(json, 3, "left_leg", 0, 0, 0));
        return new ArmorStandPose(head, body, rightArm, leftArm, rightLeg, leftLeg);
    }

    public static void register(String name, ArmorStandPose pose) {
        POSES.put(name, pose);
    }

    private static Rotations makeRotations(float[] floats) {
        return new Rotations(floats[0], floats[1], floats[2]);
    }

    public static class ArmorStandPose {

        public static final Rotations EMPTY_ROTATION = new Rotations(0, 0, 0);

        final Rotations head;
        final Rotations body;
        final Rotations rightArm;
        final Rotations leftArm;
        final Rotations rightLeg;
        final Rotations leftLeg;

        public ArmorStandPose(Rotations head, Rotations body, Rotations rightArm, Rotations leftArm, Rotations rightLeg, Rotations leftLeg) {
            this.head = head;
            this.body = body;
            this.rightArm = rightArm;
            this.leftArm = leftArm;
            this.rightLeg = rightLeg;
            this.leftLeg = leftLeg;
        }

        public void set(ArmorStandEntity armorStandEntity) {
            armorStandEntity.setHeadRotation(this.head);
            armorStandEntity.setBodyRotation(this.body);
            armorStandEntity.setRightArmRotation(this.rightArm);
            armorStandEntity.setLeftArmRotation(this.leftArm);
            armorStandEntity.setRightLegRotation(this.rightLeg);
            armorStandEntity.setLeftLegRotation(this.leftLeg);
        }

        public JsonObject serialize() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("head", serializeRotation(this.head));
            jsonObject.add("body", serializeRotation(this.body));
            jsonObject.add("right_arm", serializeRotation(this.rightArm));
            jsonObject.add("left_arm", serializeRotation(this.leftArm));
            jsonObject.add("right_leg", serializeRotation(this.rightLeg));
            jsonObject.add("left_leg", serializeRotation(this.leftLeg));
            return jsonObject;
        }

        public static JsonArray serializeRotation(Rotations rotations) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(rotations.getX());
            jsonArray.add(rotations.getY());
            jsonArray.add(rotations.getZ());
            return jsonArray;
        }
    }

}
