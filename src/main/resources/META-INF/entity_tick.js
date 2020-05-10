function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'Entity#tick': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.Entity',
                'methodName': 'func_70071_h_',
                'methodDesc': '()V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;

                var preInstructions = new InsnList();

                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/util/AsmHooks",
                    //String name
                    "entityTick",
                    //String descriptor
                    "(Lnet/minecraft/entity/Entity;)V",
                    //boolean isInterface
                    false
                ));

                instructions.insert(preInstructions);

                return methodNode;
            }
        }
    }
}