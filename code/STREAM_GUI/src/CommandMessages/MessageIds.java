/*
 This file is part of the STREAM Graphical User Interface program (STREAM GUI).

 STREAM GUI is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the License,
 or (at your option) any later version.

 The STREAM GUI software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with the software.  If not, see <http://www.gnu.org/licenses/>.
*/
package CommandMessages;

/**
 *
 * @author Jake
 */
public enum MessageIds
{
    eeCmdDisconnect,            //0
    eeCmdSetMotionConfig,       //1
    eeCmdSetSoundConfig,        //2
    eeCmdSetBladeConfig,        //3
    eeCmdRequestMotionConfig,   //4
    eeCmdRequestSoundConfig,    //5
    eeCmdRequestBladeConfig,    //6
    eeCmdRequestAck,            //7
    eeCmdRequestProtocolVersion,//8
    eeCmdRequestFirmwareVersion,//9
    eeCmdRequestProductId,      //10
    eeCmdBladeControlEnable,    //11
    eeCmdBladeControlSetEffect, //12
    eeCmdBladeControlSetChannel,//13
    eeCmdBladeControlPower,     //14
    eeCmdSoundControlEnable,    //15
    eeCmdSoundControlSetVolume, //16
    eeCmdLoadDefaultSettings,   //17
    eeCmdSaveSettngs,           //18
    eeCmdPowerUpPowerDownTest,  //19
    eeCmdSoundControlPlaySound, //20
    eeCmdSoundControlStop,      //21
    eeCmdRequestColorPresets,   //22
    eeCmdSetColorPresets,       //23
    eeCmdHumRestartTest,        //24
    eeCmdRequestSaberInfo,      //25
    eeCmdSwitchProfile,         //26
    eeCmdRequestOptionsConfig,  //27
    eeCmdSetOptionsConfig,      //28
    eeCmdLockupTest             //29
}
