#include <jni.h>
#include <string>

//
// Created by FrTech on 21-Jan-23.
//

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_depvt_live_football_tv_ui_app_activities_MainActivity_getStringArray38(JNIEnv *env,jobject thiz) {
    jobjectArray strarr = env->NewObjectArray(10, env->FindClass("java/lang/String"), nullptr);
    std::string Ot = "2xDpEMdb!334fs9Don5bnvbw4%BK##dwEVF2yIvt";	env->SetObjectArrayElement(strarr, 0, env->NewStringUTF(Ot.c_str()));
    std::string tn4 = "d7SYYOC39od#Ntr7vbhmkraEuqEkAql8o9w$pAC0";	env->SetObjectArrayElement(strarr, 1, env->NewStringUTF(tn4.c_str()));
    std::string CbE = "At7W3RKwBDsolHAzxdfPA$VMollw9tePZjyodG82";	env->SetObjectArrayElement(strarr, 2, env->NewStringUTF(CbE.c_str()));
    std::string uKs = "D$SA2nmx7n73&naddnc5uJ9a$%reBS18RGH0z^fl";	env->SetObjectArrayElement(strarr, 3, env->NewStringUTF(uKs.c_str()));
    std::string TO = "YMKx^1vLeX9nieUhhocOQxr@SOvz3t#&W9$Pg^%c";	env->SetObjectArrayElement(strarr, 4, env->NewStringUTF(TO.c_str()));
    std::string WO0 = "sk&%D#484okkdfeb^H6KRj67yz5CyE!aa6!8JKmH";	env->SetObjectArrayElement(strarr, 5, env->NewStringUTF(WO0.c_str()));
    std::string HXI = "nxCNdASBNr98ll05vbzfAIt8v#zFe4&#HA^48qyo";	env->SetObjectArrayElement(strarr, 6, env->NewStringUTF(HXI.c_str()));
    std::string OPd = "Z7CUmgn%F!zm@mnnkI1G!l5z&MWB#E8HFvdJxYp&";	env->SetObjectArrayElement(strarr, 7, env->NewStringUTF(OPd.c_str()));
    std::string Y = "XAUIO17BS31N&4gATsdtazKmue#B7uO28@^auOe%";	env->SetObjectArrayElement(strarr, 8, env->NewStringUTF(Y.c_str()));
    std::string oBp = "qvvUlZP2EQ1WM9bujC#bBundfymE28O^R!jXLVGB";	env->SetObjectArrayElement(strarr, 9, env->NewStringUTF(oBp.c_str()));

    return strarr;
}