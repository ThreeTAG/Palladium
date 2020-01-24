function initializeCoreMod() {

    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
    Opcodes = Java.type("org.objectweb.asm.Opcodes");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    IFNULL = Opcodes.IFNULL;
    ACONST_NULL = Opcodes.ACONST_NULL;
    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;

    return {
        'ItemRenderer#renderItem': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.ItemRenderer',
                'methodName': 'func_184392_a',
                'methodDesc': '(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;Z)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint1 = null;
                var injectionPoint2 = null;
                var renderItemModel_name = ASMAPI.mapMethod('func_184394_a');

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);

                    if(instruction.getOpcode() == IFNULL) {
                        injectionPoint1 = instructions.get(i);
                    }

                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.name == renderItemModel_name) {
                        injectionPoint2 = instructions.get(i);
                    }
                }

                // RenderUtil.setCurrentEntityInItemRendering(entitylivingbaseIn);
                {
                    var preInstructions = new InsnList();
                    preInstructions.add(new VarInsnNode(ALOAD, 2));
                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "net/threetag/threecore/util/client/RenderUtil",
                        //String name
                        "setCurrentEntityInItemRendering",
                        //String descriptor
                        "(Lnet/minecraft/entity/LivingEntity;)V",
                        //boolean isInterface
                        false
                    ));
                    instructions.insert(injectionPoint1, preInstructions);
                }

                // RenderUtil.setCurrentEntityInItemRendering(null);
                {
                    var preInstructions = new InsnList();
                    preInstructions.add(new InsnNode(ACONST_NULL));
                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "net/threetag/threecore/util/client/RenderUtil",
                        //String name
                        "setCurrentEntityInItemRendering",
                        //String descriptor
                        "(Lnet/minecraft/entity/LivingEntity;)V",
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
