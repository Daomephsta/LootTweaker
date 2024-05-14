package leviathan143.loottweaker.common.lib;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.ASMEventHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBusInspector
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static Field 
        listenersField, 
        listenerOwnersField, 
        ASMEventHandler_subInfoField;
    static 
    {
        try
        {
            listenersField = EventBus.class.getDeclaredField("listeners");
            listenerOwnersField = EventBus.class.getDeclaredField("listenerOwners");
            ASMEventHandler_subInfoField = ASMEventHandler.class.getDeclaredField("subInfo");
            AccessibleObject.setAccessible(new Field[] {
                listenersField, 
                listenerOwnersField,
                ASMEventHandler_subInfoField
            }, true);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException e)
        {
            LOGGER.error("Could not scan Forge event bus. Report to LootTweaker", e);
            listenersField = listenerOwnersField = ASMEventHandler_subInfoField = null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Stream<Listener> getListeners(EventBus bus)
    {
        if (listenersField == null)
            return Stream.empty();
        
        Map<Object, ? extends List<IEventListener>> listenersByClass;
        Map<Object, ModContainer> listenerOwners;
        try
        {
            listenersByClass = (Map<Object, ? extends List<IEventListener>>) listenersField.get(bus);
            listenerOwners = (Map<Object, ModContainer>) listenerOwnersField.get(bus);
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            LOGGER.error("Could not scan Forge event bus. Report to LootTweaker", e);
            return Stream.empty();
        }
        
        return listenersByClass.entrySet().stream().flatMap(entry -> 
        {
           ModContainer owner = listenerOwners.get(entry.getKey());
           return entry.getValue().stream()
               .filter(ASMEventHandler.class::isInstance)
               .map(listener -> Listener.fromASMEventHandler(owner, (ASMEventHandler) listener))
               .filter(Optional::isPresent)
               .map(Optional::get);
        });
    }

    public static class Listener
    {
        public final Class<?> eventType;
        public final ModContainer owner;
        public final EventPriority priority;
        private String humanReadable;
        
        private Listener(Class<?> eventType, ModContainer owner, EventPriority priority, String humanReadable)
        {
            this.eventType = eventType;
            this.owner = owner;
            this.priority = priority;
            this.humanReadable = humanReadable;
        }
        
        static Optional<Listener> fromASMEventHandler(ModContainer owner, ASMEventHandler listener)
        {
            Class<?> eventType;
            EventPriority priority;
            try
            {
                // Cursed, but the info isn't otherwise available
                String info = listener.toString();
                try
                {
                    String eventTypeName = info.substring(info.indexOf("(L") + "(L".length(), info.indexOf(";)"))
                        .replace('/', '.');
                    eventType = Class.forName(eventTypeName);
                }
                catch (ClassNotFoundException e)
                {
                    LOGGER.error("Could not get event type for listener {}. Report to LootTweaker", listener, e);
                    return Optional.empty();
                }
            }
            catch (Exception e)
            {
                LOGGER.error("Could not inspect listener {}. Report to LootTweaker", listener, e);
                return Optional.empty();
            }

            try
            {
                SubscribeEvent subInfo = (SubscribeEvent) ASMEventHandler_subInfoField.get(listener);
                priority = subInfo.priority();
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.error("Could not get SubscribeEvent for listener {}. Report to LootTweaker", listener, e);
                return Optional.empty();
            }
            return Optional.of(new Listener(eventType, owner, priority, listener.toString()));
        }
        
        @Override
        public String toString()
        {
            return humanReadable;
        }
    }
}
