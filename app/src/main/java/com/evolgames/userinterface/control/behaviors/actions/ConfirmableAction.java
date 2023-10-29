package com.evolgames.userinterface.control.behaviors.actions;

public class ConfirmableAction{

    private final String prompt;
    private final Action action;
    public ConfirmableAction(String prompt, Action action){
        this.prompt = prompt;
        this.action = action;
    }
    public void onConfirm(){
        this.action.performAction();
    }
    public void onCancel(){
        this.actionCancel();
    }
    public void actionCancel(){}

    public String getPrompt() {
        return prompt;
    }
}
