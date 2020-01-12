function initializeCoreMod() {
    var Opcodes = Java.type("org.objectweb.asm.Opcodes");
    var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    var IRETURN = Opcodes.IRETURN;
    var INVOKESTATIC = Opcodes.INVOKESTATIC;
    var INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    var ALOAD = Opcodes.ALOAD;

    var transformer = function(methodNode) {
        // Do stuff with classNode
        var instructions = methodNode.instructions;
        var return_statements = [];
        for(var i = 0; i < instructions.size(); ++i) {
            var instruction = instructions.get(i);
            if(instruction.getOpcode() == IRETURN) {
                return_statements.push(instructions.get(i-1));
            }
        }

        var toInject = new InsnList();
        toInject.add(new VarInsnNode(ALOAD, 1)); // World
        toInject.add(new VarInsnNode(ALOAD, 4)); // Pos
        toInject.add(new MethodInsnNode(
            //int opcode
            INVOKESTATIC,
            //String owner
            "com/davenonymous/riddlechests/event/GenerateChestEvent",
            //String name
            "fireEvent",
            //String descriptor
            "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)V",
            //boolean isInterface
            false
        ));

        for(var i = 0; i < return_statements.length; i++) {
            instructions.insertBefore(return_statements[i], toInject);
        }

        return methodNode;
    }

    return {
        'StructurePiece.generateChestObf': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.structure.StructurePiece',
                "methodName": "func_191080_a",
                "methodDesc": "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/MutableBoundingBox;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/block/BlockState;)Z"
            },
            'transformer': transformer
        },
        'StructurePiece.generateChest': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.structure.StructurePiece',
                "methodName": "generateChest",
                "methodDesc": "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/MutableBoundingBox;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/block/BlockState;)Z"
            },
            'transformer': transformer
        }
    }
}