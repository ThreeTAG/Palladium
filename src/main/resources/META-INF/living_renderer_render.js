function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    FLOAD = Opcodes.FLOAD;
    ILOAD = Opcodes.ILOAD;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'LivingRenderer#render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
                'methodName': 'func_225623_a_',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint = null;
                var setRotationAngles_name = 'setRotationAngles';

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.name === setRotationAngles_name) {
                        injectionPoint = instruction;
                        break;
                    }
                }

                if (!injectionPoint) {
                    print("Was not able to patch LivingRenderer#render()!");
                    return methodNode;
                }

                var postInstructions = new InsnList();

                postInstructions.add(new VarInsnNode(ALOAD, 0));
                postInstructions.add(new VarInsnNode(ALOAD, 1));
                postInstructions.add(new VarInsnNode(FLOAD, 2));
                postInstructions.add(new VarInsnNode(FLOAD, 3));
                postInstructions.add(new VarInsnNode(ALOAD, 4));
                postInstructions.add(new VarInsnNode(ALOAD, 5));
                postInstructions.add(new VarInsnNode(ILOAD, 6));
                postInstructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/util/AsmHooks",
                    //String name
                    "postRotationAnglesCallback",
                    //String descriptor
                    "(Lnet/minecraft/client/renderer/entity/LivingRenderer;Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
                    //boolean isInterface
                    false
                ));

                instructions.insert(injectionPoint, postInstructions);

                return methodNode;
            }
        }
    }
}