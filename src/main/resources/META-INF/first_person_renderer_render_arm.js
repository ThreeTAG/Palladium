function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;

    return {
        'FirstPersonRenderer#renderArm': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.FirstPersonRenderer',
                'methodName': 'func_228401_a_',
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IFFLnet/minecraft/util/HandSide;)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint1 = null;
                var injectionPoint2 = null;
                var renderRightArm_name = ASMAPI.mapMethod('func_229144_a_');
                var renderLeftArm_name = ASMAPI.mapMethod('func_229146_b_');

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);

                    if (instruction.getOpcode() == INVOKEVIRTUAL) {
                        if (instruction.name == renderRightArm_name) {
                            injectionPoint1 = instructions.get(i);
                        } else if (instruction.name == renderLeftArm_name) {
                            injectionPoint2 = instructions.get(i);
                        }
                    }
                }

                instructions.set(injectionPoint1, new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/client/renderer/entity/PlayerHandRenderer",
                    //String name
                    "renderRightArm",
                    //String descriptor
                    "(Lnet/minecraft/client/renderer/entity/PlayerRenderer;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V",
                    //boolean isInterface
                    false
                ));

                instructions.set(injectionPoint2, new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/client/renderer/entity/PlayerHandRenderer",
                    //String name
                    "renderLeftArm",
                    //String descriptor
                    "(Lnet/minecraft/client/renderer/entity/PlayerRenderer;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V",
                    //boolean isInterface
                    false
                ));

                return methodNode;
            }
        }
    }
}
