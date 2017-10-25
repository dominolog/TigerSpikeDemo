package pl.cubesoft.tigerspiketest.events;

/**
 * Created by CUBESOFT on 02.10.2017.
 */

public class GroupEvent {
    private final EventType eventType;
    private final String elementId;
    private final String parentElementId;

    public enum EventType {
        GROUP_ELEMENT_CREATED,
        GROUP_ELEMENT_DELETED,
    }

    public GroupEvent(EventType eventType, String elementId, String parentElementId) {
        this.eventType = eventType;
        this.elementId = elementId;
        this.parentElementId = parentElementId;
    }
}
