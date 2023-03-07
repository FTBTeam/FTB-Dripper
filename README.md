# FTB Dripper

A simple mod allowing blocks to be slowly transformed into other blocks by dripping a fluid on them.

Use https://github.com/FTBTeam/FTB-Mods-Issues for any mod issues

## Recipes

No recipes are included by default: add your own recipes via datapack, KubeJS, CraftTweaker, etc.

Example recipe JSON:

```json
{
  "type": "ftbdripper:drip",
  "inputBlock": "minecraft:dirt",
  "outputBlock": "minecraft:diamond_block",
  "fluid": "minecraft:water",
  "chance": 1.0
}
```

* "inputBlock" is a blockstate, so can include properties (e.g. `minecraft:camp_fire[lit=true]`)
* "outputBlock" is just a block
* "fluid" is the fluid which must be dripped from the Dripper
* "chance" is optional and defaults to 1.0 (valid range: 0.0 -> 1.0)
