function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    DLOAD = Opcodes.DLOAD;
    GETFIELD = Opcodes.GETFIELD;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'EntityRendererManager#renderEntity': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.EntityRendererManager',
                'methodName': 'func_188391_a',
                'methodDesc': '(Lnet/minecraft/entity/Entity;DDDFFZ)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint1 = null;
                var injectionPoint2 = null;
                var textureManager_name = ASMAPI.mapField('field_78724_e');
                var debugBoundingBox_name = ASMAPI.mapField('field_85095_o');

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);

                    if (instruction.getOpcode() == GETFIELD && instruction.name == textureManager_name && !injectionPoint1) {
                        injectionPoint1 = instructions.get(i + 2);
                    }

                    if (instruction.getOpcode() == GETFIELD && instruction.name == debugBoundingBox_name && !injectionPoint2) {
                        injectionPoint2 = instructions.get(i - 1);
                    }
                }

                if (!injectionPoint1 || !injectionPoint2) {
                    print("Was not able to patch Entity#getBoundingBox()!");
                    return methodNode;
                }

                // Hook 1
                {
                    var preInstructions = new InsnList();

                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "com/mojang/blaze3d/platform/GlStateManager",
                        //String name
                        "pushMatrix",
                        //String descriptor
                        "()V",
                        //boolean isInterface
                        false
                    ));
                    preInstructions.add(new VarInsnNode(ALOAD, 1));
                    preInstructions.add(new VarInsnNode(DLOAD, 2));
                    preInstructions.add(new VarInsnNode(DLOAD, 4));
                    preInstructions.add(new VarInsnNode(DLOAD, 6));
                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "net/threetag/threecore/sizechanging/SizeManager",
                        //String name
                        "scaleEntity",
                        //String descriptor
                        "(Lnet/minecraft/entity/Entity;DDD)V",
                        //boolean isInterface
                        false
                    ));

                    instructions.insert(injectionPoint1, preInstructions);
                }

                // Hook 2
                {
                    var preInstructions = new InsnList();

                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "com/mojang/blaze3d/platform/GlStateManager",
                        //String name
                        "popMatrix",
                        //String descriptor
                        "()V",
                        //boolean isInterface
                        false
                    ));

                    instructions.insert(injectionPoint2, preInstructions);
                }

                return methodNode;
            }
        }
    }
}