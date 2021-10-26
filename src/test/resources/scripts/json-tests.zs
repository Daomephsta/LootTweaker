#debug
import loottweaker.vanilla.loot.LootCondition;
import loottweaker.vanilla.loot.LootFunction;

// Tests Maps, booleans, and Strings
// 2nd-level members don't seem to be wrapped in JsonValues
var enchant_with_levels = {
	"levels": {
	  "min": 30.0,
	  "max": 49.0
	},
	"treasure": true,
	"function": "minecraft:enchant_with_levels"
} as LootFunction;
var entity_properties = {
	"condition": "minecraft:entity_properties",
    "entity": "this",
    "properties": {"on_fire": true}
} as LootCondition;

// Test doubles, floats
var random_chance_double = {
	"condition": "minecraft:random_chance",
	"chance": 0.5 as double
} as LootCondition;
var random_chance_float = {
	"condition": "minecraft:random_chance",
	"chance": 0.5 as float
} as LootCondition;

// Test longs, ints, shorts, and bytes
var set_count_long = {
	"function": "minecraft:set_count",
	"count": 2 as long
} as LootFunction;
var set_count_int = {
	"function": "minecraft:set_count",
	"count": 2 as int
} as LootFunction;
var set_count_short = {
	"function": "minecraft:set_count",
	"count": 2 as short
} as LootFunction;
var set_count_byte = {
	"function": "minecraft:set_count",
	"count": 2 as byte
} as LootFunction;

// Test lists
var enchant_randomly  = {
	"function": "minecraft:enchant_randomly",
	"enchantments": ["minecraft:sharpness"]
} as LootFunction;