import io.github.doothy.partystats.command.Command;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("partystats")
public class PartyStats {

    public static final String MOD_NAME = "Party Stats";
    public static final String VERSION = "1.0.0";
    public static final String AUTHORS = "Doothy";
    public static final String YEAR = "2023";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public PartyStats() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
    }

    @SubscribeEvent
    public void onCommandsRegistry(RegisterCommandsEvent event) {
        //register commands to dispatcher
        LOGGER.warn("Registering command");
        event.getDispatcher().register(Command.getCommand());
    }

}
