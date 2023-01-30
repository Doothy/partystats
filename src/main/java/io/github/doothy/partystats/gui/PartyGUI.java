package io.github.doothy.partystats.gui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import io.github.doothy.partystats.util.Util;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class PartyGUI {



    public static GooeyButton filler = GooeyButton.builder()
            .display(new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE, 1))
            .build();
    public static List<Button> pokemonButtons(PlayerPartyStorage pps){

        Pokemon[]pkmn = pps.getAll();

        List<Button> buttons = new ArrayList <>();
        for(int i = 0; i<6; i++){
            if (pkmn[i] != null){
                GooeyButton button = GooeyButton.builder()
                        .display(SpriteItemHelper.getPhoto(pkmn[i]))
                        .title(Util.formattedString((Util.formattedPokemonNameString(pkmn[i]))))
                        .lore(Util.formattedArrayList(Util.pokemonLore(pkmn[i])))
                        .build();
                buttons.add(button);
            }
        }
        return buttons;
    }

    public static GooeyPage partyPage(ServerPlayerEntity playerEntity){

        PlayerPartyStorage playerStorage = StorageProxy.getParty(playerEntity);
        List<Button> buttons = new ArrayList<>(pokemonButtons(playerStorage));

        ChestTemplate.Builder template = null;
        template = ChestTemplate.builder(3)
                .border(0, 0, 3, 9, filler);

        for(int i = 0; i < buttons.size(); i++){
            template.set(1, 1+i, buttons.get(i));
        }

        return GooeyPage.builder()
                .template(template.build())
                .build();
    }

}
