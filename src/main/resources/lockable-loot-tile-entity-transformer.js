function initializeCoreMod() {
    var Opcodes = Java.type("org.objectweb.asm.Opcodes");
    var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    var RETURN = Opcodes.RETURN;
    var INVOKESTATIC = Opcodes.INVOKESTATIC;
    var INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    var ALOAD = Opcodes.ALOAD;

    var transformer = function(methodNode) {
        // Do stuff with classNode
        var instructions = methodNode.instructions;
        var return_statements = [];
        for(var i = 0; i < instructions.size(); ++i) {
            var instruction = instructions.get(i);
            if(instruction.getOpcode() == RETURN) {
                return_statements.push(instructions.get(i));
            }
        }

        var toInject = new InsnList();
        toInject.add(new VarInsnNode(ALOAD, 0)); // IBlockReader
        toInject.add(new VarInsnNode(ALOAD, 2)); // BlockPos
        toInject.add(new MethodInsnNode(
            //int opcode
            INVOKESTATIC,
            //String owner
            "com/davenonymous/riddlechests/event/SetLootTableEvent",
            //String name
            "fireEvent",
            //String descriptor
            "(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)V",
            //boolean isInterface
            false
        ));

        for(var i = 0; i < return_statements.length; i++) {
            instructions.insertBefore(return_statements[i], toInject);
        }

        return methodNode;
    }

    return {
        'LockableLootTileEntity.setLootTableObf': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.LockableLootTileEntity',
                "methodName": "func_195479_a",
                "methodDesc": "(Lnet/minecraft/world/IBlockReader;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;)V"
            },
            'transformer': transformer
        },
        'LockableLootTileEntity.setLootTable': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.LockableLootTileEntity',
                "methodName": "setLootTable",
                "methodDesc": "(Lnet/minecraft/world/IBlockReader;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/ResourceLocation;)V"
            },
            'transformer': transformer
        }
    }
}