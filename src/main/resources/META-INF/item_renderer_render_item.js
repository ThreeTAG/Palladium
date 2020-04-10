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
                'methodName': 'func_229109_a_',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/world/World;II)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;

                // RenderUtil.setCurrentEntityInItemRendering(entitylivingbaseIn);
                {
                    var preInstructions = new InsnList();
                    preInstructions.add(new VarInsnNode(ALOAD, 1));
                    preInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "net/threetag/threecore/util/RenderUtil",
                        //String name
                        "setCurrentEntityInItemRendering",
                        //String descriptor
                        "(Lnet/minecraft/entity/LivingEntity;)V",
                        //boolean isInterface
                        false
                    ));
                    instructions.insert(preInstructions);
                }

                // RenderUtil.setCurrentEntityInItemRendering(null);
                {
                    var postInstructions = new InsnList();
                    postInstructions.add(new InsnNode(ACONST_NULL));
                    postInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "net/threetag/threecore/util/RenderUtil",
                        //String name
                        "setCurrentEntityInItemRendering",
                        //String descriptor
                        "(Lnet/minecraft/entity/LivingEntity;)V",
                        //boolean isInterface
                        false
                    ));
                    instructions.add(postInstructions);
                }

                return methodNode;
            }
        }
    }
}
