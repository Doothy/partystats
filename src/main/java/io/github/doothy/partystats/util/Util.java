package io.github.doothy.partystats.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;

import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static List <String> formattedArrayList(List<String> list) {

        List<String> formattedList = new ArrayList <>();
        for (String s:list) {
            formattedList.add(formattedString(s));
        }

        return formattedList;
    }
    public static String formattedString(String s){
        return s.replaceAll("&", "§");
    }
    private static final Pattern HEXPATTERN = Pattern.compile("\\{(#[a-fA-F0-9]{6})}");
    private static final String SPLITPATTERN = "((?=\\{#[a-fA-F0-9]{6}}))";

    public static ITextComponent parseHexCodes(String text, boolean removeItalics) {
        if(text == null)
            return null;
        StringTextComponent comp = new StringTextComponent("");

        String[] temp = text.split(SPLITPATTERN);
        Arrays.stream(temp).forEach(s -> {
            Matcher m = HEXPATTERN.matcher(s);
            if(m.find()) {
                Color color = Color.fromHex(m.group(1));
                s = m.replaceAll("");
                if(removeItalics)
                    comp.appendSibling(new StringTextComponent(s).setStyle(Style.EMPTY.setColor(color).setItalic(false)));
                else
                    comp.appendSibling(new StringTextComponent(s).setStyle(Style.EMPTY.setColor(color)));
            } else {
                comp.appendSibling(new StringTextComponent(s));
            }
        });

        return comp;
    }

    public static String formattedPokemonNameString(Pokemon p) {

        String s = "&b%pokemon% &eLvl: %lvl%"
                .replace("%pokemon%", p.getDisplayName())
                .replace("%lvl%", String.valueOf(p.getPokemonLevel()));

        if (p.isShiny())
            s += " &6Shiny";

        return s;
    }

    public static int[] getEvsArray(Pokemon pokemon){
        return new int[]{
                pokemon.getEVs().getStat(BattleStatsType.HP),
                pokemon.getEVs().getStat(BattleStatsType.ATTACK),
                pokemon.getEVs().getStat(BattleStatsType.DEFENSE),
                pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK),
                pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE),
                pokemon.getEVs().getStat(BattleStatsType.SPEED)};
    }

    public static double getEVSPercentage(int decimalPlaces, Pokemon p) {
        int total = 0;
        int[] evs = getEvsArray(p);

        for (int evStat : evs) {
            total += evStat;
        }
        double percentage = (double)total / 510.0D * 100.0D;
        return Math.floor(percentage * Math.pow(10.0D, decimalPlaces)) / Math.pow(10.0D, decimalPlaces);
    }


    public static ItemStack getPhoto(Pokemon pokemon) {
        return SpriteItemHelper.getPhoto(pokemon);
    }

    public static ArrayList<String> pokemonLore(Pokemon pokemon){

        ArrayList<String> list = new ArrayList<>();

        list.add("&7Ball:&e " + pokemon.getBall().getName());
        list.add("&7Ability:&e " + pokemon.getAbility().getName());
        list.add("&7Nature:&e " + pokemon.getNature().name());
        list.add("&7Gender:&e " + pokemon.getGender().name());
        list.add("&7Size:&e " + pokemon.getGrowth().name());
        if (pokemon.getPalette().getTexture() != null) {
            if (!pokemon.getPalette().getTexture().toString().isEmpty()) {
                list.add("&5Custom Texture: &b" + pokemon.getPalette().getName());
            }
        }
        list.add("&7IVS: (&f%ivs%%&7)".replace("%ivs%", String.valueOf(pokemon.getIVs().getPercentage(1))));
        list.add("&cHP: %hp% &7/ &6Atk: %atk% &7/ &eDef: %def%"
                .replace("%hp%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.HP)))
                .replace("%atk%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.ATTACK)))
                .replace("%def%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.DEFENSE)))
        );
        list.add("&9SpA: %spa% &7/ &aSpD: %spd% &7/ &dSpe: %spe%"
                .replace("%spa%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK)))
                .replace("%spd%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE)))
                .replace("%spe%", String.valueOf(pokemon.getIVs().getStat(BattleStatsType.SPEED)))
        );
        list.add("&7EVS: (&f%evs%%&7)".replace("%evs%", String.valueOf(getEVSPercentage(1, pokemon))));
        list.add("&cHP: %hp% &7/ &6Atk: %atk% &7/ &eDef: %def%"
                .replace("%hp%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.HP)))
                .replace("%atk%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.ATTACK)))
                .replace("%def%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.DEFENSE)))
        );
        list.add("&9SpA: %spa% &7/ &aSpD: %spd% &7/ &dSpe: %spe%"
                .replace("%spa%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK)))
                .replace("%spd%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE)))
                .replace("%spe%", String.valueOf(pokemon.getEVs().getStat(BattleStatsType.SPEED)))
        );
        for (Attack a:pokemon.getMoveset().attacks) {
            if (a == null)
                continue;
            list.add("&7- " + a.getActualMove().getAttackName());
        }

        return list;
    }

    public static ItemStack convertPokemonToItem(Pokemon pokemon) {
        ItemStack stack = getPhoto(pokemon);
        stack.setDisplayName(new StringTextComponent(formattedString(formattedPokemonNameString(pokemon))));
        stack.getOrCreateChildTag("display").put("Lore", setLoreNBT(pokemonLore(pokemon)));
        stack.getTag().putBoolean("convertablePokemon", true);
        stack.setTag(pokemon.writeToNBT(stack.getTag()));
        return stack;
    }

    public static ListNBT setLoreNBT(List <String> loreArray) {
        ListNBT lore = new ListNBT();

        for (String line : loreArray) {
            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(parseHexCodes(line.replaceAll("&([0-9a-fk-or])", "\u00a7$1").replaceAll("minecraft:", "").replaceAll("_", " ").replaceAll("pixelmon:", ""), true))));
        }
        return lore;
    }

}
