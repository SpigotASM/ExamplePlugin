# ExamplePlugin
An example plugin which changes the pig temptation item from CARROT_ON_A_STICK to DIAMOND by modifying the bytecode of EntityPig.class instead of overriding minecraft functionality via bukkit. Obviously this isnt the best excample as a use case, but it shows what is possible.

* Have a look at the asm.json resource included in the plugin [asm.json](https://github.com/SpigotASM/ExamplePlugin/blob/master/src/main/resources/asm.json)
* Have a look at the class transformer [PigTemptationTransformer.java](https://github.com/SpigotASM/ExamplePlugin/blob/master/src/main/java/com/example/transformers/PigTemptationTransformer.java)
* Take a look at the setup guide to follow this [here](https://github.com/SpigotASM/ClassTweaker/blob/master/README.md)
