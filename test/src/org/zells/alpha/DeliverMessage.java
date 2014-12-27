package org.zells.alpha;

import org.junit.Assert;
import org.junit.Test;
import org.zells.alpha.dynamic.BaseCell;

public class DeliverMessage {

    @Test
    public void nativeResponse() {
        BaseCell cell = new BaseCell() {
            @Override
            public Object receive(Object message) {
                return "Received " + message;
            }
        };
        Assert.assertEquals("Received foo", cell.receive("foo"));
    }

    @Test
    public void addChild() {
        BaseCell parent = new BaseCell();
        BaseCell foo = new BaseCell();
        parent.add("foo", foo);
        Assert.assertEquals(parent.get("foo"), foo);
    }

    @Test
    public void nonExistingChild() {
        BaseCell cell = new BaseCell();
        try {
            cell.get("foo");
            Assert.fail("Should have thrown exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void removeChild() {
        BaseCell parent = new BaseCell();
        BaseCell foo = new BaseCell();
        parent.add("foo", foo);
        Assert.assertEquals(1, parent.children().size());

        parent.remove("foo");
        Assert.assertEquals(0, parent.children().size());
    }

    @Test
    public void inheritChild() {
        BaseCell stem = new BaseCell();
        BaseCell cell = new BaseCell(stem);
        BaseCell child = new BaseCell();

        stem.add("foo", child);
        Assert.assertEquals(child, cell.get("foo"));
    }

}
