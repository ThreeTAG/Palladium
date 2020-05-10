function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
    InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");

    ALOAD = Opcodes.ALOAD;
    ARETURN = Opcodes.ARETURN;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'NetworkPlayerInfo#getLocationSkin': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.network.play.NetworkPlayerInfo',
                'methodName': 'func_178837_g',
                'methodDesc': '()Lnet/minecraft/util/ResourceLocation;'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;

                instructions.clear();

                instructions.add(new VarInsnNode(ALOAD, 0));
                instructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/util/AsmHooks",
                    //String name
                    "getLocationSkin",
                    //String descriptor
                    "(Lnet/minecraft/client/network/play/NetworkPlayerInfo;)Lnet/minecraft/util/ResourceLocation;",
                    //boolean isInterface
                    false
                ));
                instructions.add(new InsnNode(ARETURN));

                return methodNode;
            }
        }
    }
}