function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    FieldInsnNode = Java.type("org.objectweb.asm.tree.FieldInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
    InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");

    ALOAD = Opcodes.ALOAD;
    ASTORE = Opcodes.ASTORE;
    GETFIELD = Opcodes.GETFIELD;
    PUTFIELD = Opcodes.PUTFIELD;
    FMUL = Opcodes.FMUL;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKESPECIAL = Opcodes.INVOKESPECIAL;

    return {
        'Entity#recalculateSize': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.Entity',
                'methodName': 'func_213323_x_',
                'methodDesc': '()V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint = null;
                var getEyeHeightForge_name = 'getEyeHeightForge';
                var size_name = ASMAPI.mapField('field_213325_aI');
                var y_name = ASMAPI.mapField('field_189983_j');
                var eyeHeight_name = ASMAPI.mapField('field_213326_aJ');

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);
                    if (instruction.getOpcode() == INVOKESPECIAL && instruction.name == getEyeHeightForge_name) {
                        injectionPoint = instructions.get(i + 1);
                        break;
                    }
                }

                if (!injectionPoint) {
                    print("Was not able to patch Entity#recalculateSize()!");
                    return methodNode;
                }

                var preInstructions = new InsnList();

                preInstructions.add(new VarInsnNode(ALOAD, 3));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 2));
                preInstructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/sizechanging/SizeManager",
                    //String name
                    "getOverridenSize",
                    //String descriptor
                    "(Lnet/minecraft/entity/EntitySize;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Pose;)Lnet/minecraft/entity/EntitySize;",
                    //boolean isInterface
                    false
                ));
                preInstructions.add(new VarInsnNode(ASTORE, 3));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 3));
                preInstructions.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/Entity", size_name, 'Lnet/minecraft/entity/EntitySize;'));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/Entity", eyeHeight_name, 'F'));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 2));
                preInstructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/sizechanging/SizeManager",
                    //String name
                    "getSize",
                    //String descriptor
                    "(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Pose;)Lnet/minecraft/util/math/Vec2f;",
                    //boolean isInterface
                    false
                ));
                preInstructions.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/math/Vec2f", y_name, 'F'));
                preInstructions.add(new InsnNode(FMUL));
                preInstructions.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/Entity", eyeHeight_name, 'F'));

                instructions.insert(injectionPoint, preInstructions);

                print("HALLO recalculateSize was patched!");

                return methodNode;
            }
        }
    }
}