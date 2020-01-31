/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.helper;
import net.ausiasmarch.setting.ConfigurationSettings;
import net.ausiasmarch.setting.ConfigurationSettings.EnvironmentConstans;

/**
 *
 * @author alejandro
 */
public class TraceHelper {
    public static void trace(String s) {
         if (ConfigurationSettings.environment == EnvironmentConstans.Debug) {
            System.out.println(s);
        }
    }
}
