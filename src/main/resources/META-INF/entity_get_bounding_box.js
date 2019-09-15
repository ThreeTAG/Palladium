function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    ASTORE = Opcodes.ASTORE;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'Entity#getBoundingBox': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.Entity',
                'methodName': 'func_213321_d',
                'methodDesc': '(Lnet/minecraft/entity/Pose;)Lnet/minecraft/util/math/AxisAlignedBB;'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint = null;

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);
                    if (instruction.getOpcode() == INVOKEVIRTUAL) {
                        injectionPoint = instruction;
                        break;
                    }
                }

                if (!injectionPoint) {
                    print("Was not able to patch Entity#getBoundingBox()!");
                    return methodNode;
                }

                var preInstructions = new InsnList();

                preInstructions.add(new VarInsnNode(ASTORE, 2));
                preInstructions.add(new VarInsnNode(ALOAD, 2));
                preInstructions.add(new VarInsnNode(ALOAD, 0));
                preInstructions.add(new VarInsnNode(ALOAD, 1));
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

                instructions.insert(injectionPoint, preInstructions);

                return methodNode;
            }
        }
    }
}