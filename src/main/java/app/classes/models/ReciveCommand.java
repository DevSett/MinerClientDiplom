package app.classes.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReciveCommand {

    private String id;


    private boolean status;


    private boolean status_debug;


    private boolean service_reboot_rig;


    private boolean service_shutdown_rig;



}
