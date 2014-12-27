package org.zells.alpha;

import org.junit.Assert;
import org.junit.Test;
import org.zells.alpha.dynamic.ScriptedCell;

public class ScriptedResponse {

    @Test
    public void staticResponse() {
        ScriptedCell cell = new ScriptedCell();
        cell.respond("'Foo!'");
        Assert.assertEquals("Foo!", cell.receive(null));
    }

    @Test
    public void respondToMessage() {
        ScriptedCell cell = new ScriptedCell();
        cell.respond("'Hello ' + message");
        Assert.assertEquals("Hello There", cell.receive("There"));
    }

    @Test
    public void referenceSelf() {
        ScriptedCell cell = new ScriptedCell();
        cell.respond("self");
        Assert.assertEquals(cell, cell.receive(null));
    }

    @Test
    public void accessChild() {
        ScriptedCell parent = new ScriptedCell();
        ScriptedCell child = new ScriptedCell();
        parent.add("foo", child);

        parent.respond("self.foo");
        Assert.assertEquals(child, parent.receive(null));
    }

    @Test
    public void sendMessage() {
        ScriptedCell cell = new ScriptedCell();
        cell.respond("self.foo('World')");

        ScriptedCell foo = new ScriptedCell();
        cell.add("foo", foo);
        foo.respond("'Hello ' + message");

        Assert.assertEquals("Hello World", cell.receive(null));
    }

    @Test
    public void defaultResponse() {
        ScriptedCell cell = new ScriptedCell();
        Assert.assertEquals(null, cell.receive("None"));
    }

    @Test
    public void inheritResponse() {
        ScriptedCell stem = new ScriptedCell();
        ScriptedCell cell = new ScriptedCell(stem);
        stem.respond("'Hello ' + message");
        Assert.assertEquals("Hello World", cell.receive("World"));
    }

    @Test
    public void keepContextOfInheritedResponse() {
        ScriptedCell stem = new ScriptedCell();
        stem.respond("self.foo");

        ScriptedCell cell = new ScriptedCell(stem);
        ScriptedCell child = new ScriptedCell();
        cell.add("foo", child);

        Assert.assertEquals(child, cell.receive(null));
    }
}
