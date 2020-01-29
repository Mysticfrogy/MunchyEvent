package nl.hpfxd.munchyevent;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import nl.hpfxd.munchyevent.commands.*;
import nl.hpfxd.munchyevent.features.crr.CRRCommand;
import nl.hpfxd.munchyevent.features.crr.CRRTeam;

/*
* MunchyEvent by hpfxd.nl
*
* Utilities for MunchyMC Event Team members.
*/
@Mod(modid = MunchyEvent.MODID, version = MunchyEvent.VERSION)
public class MunchyEvent {
    public static final String MODID = "munchyevent";
    public static final String VERSION = "1.2";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (!MUtil.reloadEvents()) {
            System.out.println("There was an error loading events.");
        }

        try {
            MUtil.sendAnalytics();
        } catch (Exception e) {
            System.out.println("Could not send analytics.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CRRTeam.init();
        this.registerCommands();
    }

    private void registerCommands() {
        ClientCommandHandler cch = ClientCommandHandler.instance;

        CRRCommand crrc = new CRRCommand();

        cch.registerCommand(new MHostCommand());
        cch.registerCommand(new EventsCommand());
        cch.registerCommand(new PNoteCommand());
        cch.registerCommand(new MItemCommand());
        cch.registerCommand(new PKitCommand());
        cch.registerCommand(crrc);

        EventBus bus = MinecraftForge.EVENT_BUS;

        bus.register(crrc);
    }
}
