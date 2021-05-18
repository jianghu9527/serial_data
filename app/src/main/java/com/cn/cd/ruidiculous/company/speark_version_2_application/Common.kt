package com.cn.cd.ruidiculous.company.speark_version_2_application

class Common {


    companion object{
        /**
         * 消杀开关  ===开
         */
        var kill_command_open :ByteArray =  byteArrayOf(0xa1.toByte(), 0x01.toByte(), 0x01.toByte(), 0xa1.toByte() )
        /**
         * 消杀开关  ===关
         */
        var kill_command_shut =   byteArrayOf(0xa1.toByte(), 0x01.toByte(), 0x00.toByte(), 0xa1.toByte() )
        /**
         * 消杀报警===开
         */
        val kill_callpolice_open =  byteArrayOf(0xa1.toByte(), 0x02.toByte(), 0x01.toByte(), 0xa1.toByte() )
        /**
         * 消杀报警===关
         */
        val kill_callpolice_shut =  byteArrayOf(0xa1.toByte(), 0x02.toByte(), 0x00.toByte(), 0xa1.toByte() )


        /**
         * 串口返回数据类型
         */
        val   SkillDataBack_type_wate="水位";
        val   SkillDataBack_type_operation="操作";


        /**
         * 当前状态
         *  是否再消杀
         *  是否再导览
         *  是否再充电
         *  是否门口
         *
         */
        val Current_State="当前状态";



    }


}