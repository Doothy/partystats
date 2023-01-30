package io.github.doothy.partystats.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sun.org.apache.xpath.internal.operations.String;
import io.github.doothy.partystats.gui.PartyGUI;
import net.minecraft.command.Commands;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class Command {

    public static LiteralArgumentBuilder<CommandSource> getCommand(){
        return Commands.literal("partystats")
                .executes(cc -> {
                    try{
                        UIManager.openUIForcefully(cc.getSource().asPlayer(), PartyGUI.partyPage(cc.getSource().asPlayer()));
                    }
                    catch(Exception e){
                        cc.getSource().sendFeedback(new StringTextComponent("Something appears to gone wrong, please contact staff!"), true);
                        return 1;
                    }
                    return 1;
                });
    }
}
