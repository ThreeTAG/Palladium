function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'InventoryScreen#drawEntityOnScreen': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.gui.screen.inventory.InventoryScreen',
                'methodName': 'func_228187_a_',
                'methodDesc': '(IIIFFLnet/minecraft/entity/LivingEntity;)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint = null;
                var scalef_name = 'scalef';

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);

                    if (instruction.getOpcode() == INVOKESTATIC && instruction.name == scalef_name && !injectionPoint) {
                        injectionPoint = instructions.get(i);
                    }
                }

                if (!injectionPoint) {
                    print("Was not able to patch InventoryScreen#drawEntityOnScreen()!");
                    return methodNode;
                }

                var preInstructions = new InsnList();

                preInstructions.add(new VarInsnNode(ALOAD, 5));
                preInstructions.add(new MethodInsnNode(
                    //int opcode
                    INVOKESTATIC,
                    //String owner
                    "net/threetag/threecore/sizechanging/SizeManager",
                    //String name
                    "renderInInvCallback",
                    //String descriptor
                    "(Lnet/minecraft/entity/LivingEntity;)V",
                    //boolean isInterface
                    false
                ));

                instructions.insert(injectionPoint, preInstructions);

                return methodNode;
            }
        }
    }
}