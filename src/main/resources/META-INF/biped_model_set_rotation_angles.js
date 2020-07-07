function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    FLOAD = Opcodes.FLOAD;
    INVOKESTATIC = Opcodes.INVOKESTATIC;
    RETURN = Opcodes.RETURN;
    return {
        'BipedModel#setRotationAngles': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.model.BipedModel',
                'methodName': 'func_225597_a_',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFFFF)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint = null;

                for (var i = 0; i < instructions.size(); i++) {
                    if (instructions.get(i).getOpcode() === RETURN) {
                        injectionPoint = instructions.get(i - 1);
                        break;
                    }
                }

                if (!injectionPoint) {
                    print("Was not able to patch BipedModel#setRotationAngles()!");
                    return methodNode;
                }

                var preInstructions = new InsnList();
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 1));
                preInstructions.add(new VarInsnNode(FLOAD, 2));
                preInstructions.add(new VarInsnNode(FLOAD, 3));
                preInstructions.add(new VarInsnNode(FLOAD, 4));
                preInstructions.add(new VarInsnNode(FLOAD, 5));
                preInstructions.add(new VarInsnNode(FLOAD, 6));
                preInstructions.add(new MethodInsnNode(INVOKESTATIC, "net/threetag/threecore/util/AsmHooks", "setRotationAnglesCallback", "(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/entity/LivingEntity;FFFFF)V", false));
                instructions.insert(injectionPoint, preInstructions);

                return methodNode;
            }
        }
    }
}
