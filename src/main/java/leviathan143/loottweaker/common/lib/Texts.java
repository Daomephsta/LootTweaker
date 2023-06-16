package leviathan143.loottweaker.common.lib;

import java.io.File;
import java.util.function.Consumer;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;


public class Texts
{
    public static ITextComponent fileLink(File target)
    {
        return styledAsString(target, style ->
        {
            style.setClickEvent(new ClickEvent(Action.OPEN_FILE, target.toString()))
                .setUnderlined(true)
                .setColor(TextFormatting.AQUA);
        });
    }

    public static ITextComponent styledString(String text, Consumer<Style> styler)
    {
        ITextComponent styled = new TextComponentString(text);
        styler.accept(styled.getStyle());
        return styled;
    }

    public static ITextComponent styledAsString(Object text, Consumer<Style> styler)
    {
        return styledString(text.toString(), styler);
    }
}
