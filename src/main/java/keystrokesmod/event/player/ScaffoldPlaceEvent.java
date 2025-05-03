package keystrokesmod.event.player;

import keystrokesmod.eventbus.CancellableEvent;
import keystrokesmod.minecraft.MovingObjectPosition;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ScaffoldPlaceEvent extends CancellableEvent {
    private MovingObjectPosition hitResult;
    private boolean extra;
}
