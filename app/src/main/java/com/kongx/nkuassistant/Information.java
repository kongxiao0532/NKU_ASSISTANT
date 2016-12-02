package com.kongx.nkuassistant;

import android.support.annotation.NonNull;

import java.nio.Buffer;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * Created by kongx on 2016/11/17 0017.
 */

public class Information {

    //related to Personal Information
    static String name;
    static String facultyName;
    static String majorName;
    static String id;

    //related to Home Page
    static int weekCount;
    static String semester;
    static String date;

    //related to Scores
    static int studiedCourseCount;
    static int selectedCourseCount;
    static ArrayList<HashMap<String,String>> studiedCourses = new ArrayList<>();
    static ArrayList<HashMap<String,String>> selectedCourses = new ArrayList<>();
    static ArrayList<HashMap<String,String>> exams = new ArrayList<>();
    static float[] credits = new float[5];
    static float[] scores = new float[5];
    static float[] averages = new float[5];
    static float credits_All;
    static float scores_All;
    static float average_abcd;
    static float average_abcde;
    static public void resetScores(){
        for(int i = 0;i < 5;i++){
            scores[i] = credits[i] = averages[i] = 0;
        }
        credits_All = scores_All = average_abcd = average_abcde = 0;
    }

    //related to Exams
    static int examCount;


    //related to Internet Connection
    static boolean ifFirstStart;
    static final String PREFS_NAME = "NKUFile";
    static final String COURSE_PREFS_NAME = "CourseFile";
    static final String EXAM_PREFS_NAME = "ExamFile";
    static final String webUrl = "http://222.30.49.10";
    static final String js = "var biRadixBase=2,biRadixBits=16,bitsPerDigit=biRadixBits,biRadix=65536,biHalfRadix=biRadix>>>1,biRadixSquared=biRadix*biRadix,maxDigitVal=biRadix-1,maxInteger=9999999999999998,maxDigits,ZERO_ARRAY,bigZero,bigOne,result;function BigInt(a){this.digits=\"boolean\"==typeof a&&1==a?null:ZERO_ARRAY.slice(0);this.isNeg=!1}function setMaxDigits(a){maxDigits=a;ZERO_ARRAY=Array(maxDigits);for(a=0;a<ZERO_ARRAY.length;a++)ZERO_ARRAY[a]=0;bigZero=new BigInt;bigOne=new BigInt;bigOne.digits[0]=1}setMaxDigits(20);\n" +
            "var dpl10=15;function biFromNumber(a){var c=new BigInt;c.isNeg=0>a;a=Math.abs(a);for(var b=0;0<a;)c.digits[b++]=a&maxDigitVal,a=Math.floor(a/biRadix);return c}var lr10=biFromNumber(1E15);\n" +
            "function biFromDecimal(a){for(var c=\"-\"==a.charAt(0),b=c?1:0,d;b<a.length&&\"0\"==a.charAt(b);)++b;if(b==a.length)d=new BigInt;else{var e=(a.length-b)%dpl10;0==e&&(e=dpl10);d=biFromNumber(Number(a.substr(b,e)));for(b+=e;b<a.length;)d=biAdd(biMultiply(d,lr10),biFromNumber(Number(a.substr(b,dpl10)))),b+=dpl10;d.isNeg=c}return d}function biCopy(a){var c=new BigInt(!0);c.digits=a.digits.slice(0);c.isNeg=a.isNeg;return c}function reverseStr(a){for(var c=\"\",b=a.length-1;-1<b;--b)c+=a.charAt(b);return c}\n" +
            "var hexatrigesimalToChar=\"0123456789abcdefghijklmnopqrstuvwxyz\".split(\"\");function biToString(a,c){var b=new BigInt;b.digits[0]=c;for(var d=biDivideModulo(a,b),e=hexatrigesimalToChar[d[1].digits[0]];1==biCompare(d[0],bigZero);)d=biDivideModulo(d[0],b),digit=d[1].digits[0],e+=hexatrigesimalToChar[d[1].digits[0]];return(a.isNeg?\"-\":\"\")+reverseStr(e)}\n" +
            "function biToDecimal(a){var c=new BigInt;c.digits[0]=10;for(var b=biDivideModulo(a,c),d=String(b[1].digits[0]);1==biCompare(b[0],bigZero);)b=biDivideModulo(b[0],c),d+=String(b[1].digits[0]);return(a.isNeg?\"-\":\"\")+reverseStr(d)}var hexToChar=\"0123456789abcdef\".split(\"\");function digitToHex(a){for(var c=\"\",b=0;4>b;++b)c+=hexToChar[a&15],a>>>=4;return reverseStr(c)}function biToHex(a){var c=\"\";biHighIndex(a);for(var b=biHighIndex(a);-1<b;--b)c+=digitToHex(a.digits[b]);return c}\n" +
            "function charToHex(a){return 48<=a&&57>=a?a-48:65<=a&&90>=a?10+a-65:97<=a&&122>=a?10+a-97:0}function hexToDigit(a){for(var c=0,b=Math.min(a.length,4),d=0;d<b;++d)c<<=4,c|=charToHex(a.charCodeAt(d));return c}function biFromHex(a){for(var c=new BigInt,b=a.length,d=0;0<b;b-=4,++d)c.digits[d]=hexToDigit(a.substr(Math.max(b-4,0),Math.min(b,4)));return c}\n" +
            "function biFromString(a,c){var b=\"-\"==a.charAt(0),d=b?1:0,e=new BigInt,g=new BigInt;g.digits[0]=1;for(var f=a.length-1;f>=d;f--)var h=a.charCodeAt(f),h=charToHex(h),h=biMultiplyDigit(g,h),e=biAdd(e,h),g=biMultiplyDigit(g,c);e.isNeg=b;return e}function biDump(a){return(a.isNeg?\"-\":\"\")+a.digits.join(\" \")}\n" +
            "function biAdd(a,c){var b;if(a.isNeg!=c.isNeg)c.isNeg=!c.isNeg,b=biSubtract(a,c),c.isNeg=!c.isNeg;else{b=new BigInt;for(var d=0,e=0;e<a.digits.length;++e)d=a.digits[e]+c.digits[e]+d,b.digits[e]=d%biRadix,d=Number(d>=biRadix);b.isNeg=a.isNeg}return b}\n" +
            "function biSubtract(a,c){var b;if(a.isNeg!=c.isNeg)c.isNeg=!c.isNeg,b=biAdd(a,c),c.isNeg=!c.isNeg;else{b=new BigInt;for(var d,e=d=0;e<a.digits.length;++e)d=a.digits[e]-c.digits[e]+d,b.digits[e]=d%biRadix,0>b.digits[e]&&(b.digits[e]+=biRadix),d=0-Number(0>d);if(-1==d){for(e=d=0;e<a.digits.length;++e)d=0-b.digits[e]+d,b.digits[e]=d%biRadix,0>b.digits[e]&&(b.digits[e]+=biRadix),d=0-Number(0>d);b.isNeg=!a.isNeg}else b.isNeg=a.isNeg}return b}\n" +
            "function biHighIndex(a){for(var c=a.digits.length-1;0<c&&0==a.digits[c];)--c;return c}function biNumBits(a){var c=biHighIndex(a);a=a.digits[c];var c=(c+1)*bitsPerDigit,b;for(b=c;b>c-bitsPerDigit&&0==(a&32768);--b)a<<=1;return b}function biMultiply(a,c){for(var b=new BigInt,d,e=biHighIndex(a),g=biHighIndex(c),f,h=0;h<=g;++h){d=0;f=h;for(var k=0;k<=e;++k,++f)d=b.digits[f]+a.digits[k]*c.digits[h]+d,b.digits[f]=d&maxDigitVal,d>>>=biRadixBits;b.digits[h+e+1]=d}b.isNeg=a.isNeg!=c.isNeg;return b}\n" +
            "function biMultiplyDigit(a,c){var b,d,e=new BigInt;b=biHighIndex(a);for(var g=d=0;g<=b;++g)d=e.digits[g]+a.digits[g]*c+d,e.digits[g]=d&maxDigitVal,d>>>=biRadixBits;e.digits[1+b]=d;return e}function arrayCopy(a,c,b,d,e){for(e=Math.min(c+e,a.length);c<e;++c,++d)b[d]=a[c]}var highBitMasks=[0,32768,49152,57344,61440,63488,64512,65024,65280,65408,65472,65504,65520,65528,65532,65534,65535];\n" +
            "function biShiftLeft(a,c){var b=Math.floor(c/bitsPerDigit),d=new BigInt;arrayCopy(a.digits,0,d.digits,b,d.digits.length-b);for(var b=c%bitsPerDigit,e=bitsPerDigit-b,g=d.digits.length-1,f=g-1;0<g;--g,--f)d.digits[g]=d.digits[g]<<b&maxDigitVal|(d.digits[f]&highBitMasks[b])>>>e;d.digits[0]=d.digits[g]<<b&maxDigitVal;d.isNeg=a.isNeg;return d}var lowBitMasks=[0,1,3,7,15,31,63,127,255,511,1023,2047,4095,8191,16383,32767,65535];\n" +
            "function biShiftRight(a,c){var b=Math.floor(c/bitsPerDigit),d=new BigInt;arrayCopy(a.digits,b,d.digits,0,a.digits.length-b);for(var b=c%bitsPerDigit,e=bitsPerDigit-b,g=0,f=g+1;g<d.digits.length-1;++g,++f)d.digits[g]=d.digits[g]>>>b|(d.digits[f]&lowBitMasks[b])<<e;d.digits[d.digits.length-1]>>>=b;d.isNeg=a.isNeg;return d}function biMultiplyByRadixPower(a,c){var b=new BigInt;arrayCopy(a.digits,0,b.digits,c,b.digits.length-c);return b}\n" +
            "function biDivideByRadixPower(a,c){var b=new BigInt;arrayCopy(a.digits,c,b.digits,0,b.digits.length-c);return b}function biModuloByRadixPower(a,c){var b=new BigInt;arrayCopy(a.digits,0,b.digits,0,c);return b}function biCompare(a,c){if(a.isNeg!=c.isNeg)return 1-2*Number(a.isNeg);for(var b=a.digits.length-1;0<=b;--b)if(a.digits[b]!=c.digits[b])return a.isNeg?1-2*Number(a.digits[b]>c.digits[b]):1-2*Number(a.digits[b]<c.digits[b]);return 0}\n" +
            "function biDivideModulo(a,c){var b=biNumBits(a),d=biNumBits(c),e=c.isNeg,g,f;if(b<d)return a.isNeg?(g=biCopy(bigOne),g.isNeg=!c.isNeg,a.isNeg=!1,c.isNeg=!1,f=biSubtract(c,a),a.isNeg=!0,c.isNeg=e):(g=new BigInt,f=biCopy(a)),[g,f];g=new BigInt;f=a;for(var h=Math.ceil(d/bitsPerDigit)-1,k=0;c.digits[h]<biHalfRadix;)c=biShiftLeft(c,1),++k,++d,h=Math.ceil(d/bitsPerDigit)-1;f=biShiftLeft(f,k);b=Math.ceil((b+k)/bitsPerDigit)-1;for(d=biMultiplyByRadixPower(c,b-h);-1!=biCompare(f,d);)++g.digits[b-h],f=biSubtract(f,\n" +
            "d);for(;b>h;--b){var d=b>=f.digits.length?0:f.digits[b],m=b-1>=f.digits.length?0:f.digits[b-1],n=b-2>=f.digits.length?0:f.digits[b-2],l=h>=c.digits.length?0:c.digits[h],p=h-1>=c.digits.length?0:c.digits[h-1];g.digits[b-h-1]=d==l?maxDigitVal:Math.floor((d*biRadix+m)/l);for(var q=g.digits[b-h-1]*(l*biRadix+p),r=d*biRadixSquared+(m*biRadix+n);q>r;)--g.digits[b-h-1],q=g.digits[b-h-1]*(l*biRadix|p),r=d*biRadix*biRadix+(m*biRadix+n);d=biMultiplyByRadixPower(c,b-h-1);f=biSubtract(f,biMultiplyDigit(d,g.digits[b-\n" +
            "h-1]));f.isNeg&&(f=biAdd(f,d),--g.digits[b-h-1])}f=biShiftRight(f,k);g.isNeg=a.isNeg!=e;a.isNeg&&(g=e?biAdd(g,bigOne):biSubtract(g,bigOne),c=biShiftRight(c,k),f=biSubtract(c,f));0==f.digits[0]&&0==biHighIndex(f)&&(f.isNeg=!1);return[g,f]}function biDivide(a,c){return biDivideModulo(a,c)[0]}function biModulo(a,c){return biDivideModulo(a,c)[1]}function biMultiplyMod(a,c,b){return biModulo(biMultiply(a,c),b)}\n" +
            "function biPow(a,c){for(var b=bigOne,d=a;;){0!=(c&1)&&(b=biMultiply(b,d));c>>=1;if(0==c)break;d=biMultiply(d,d)}return b}function biPowMod(a,c,b){for(var d=bigOne;;){0!=(c.digits[0]&1)&&(d=biMultiplyMod(d,a,b));c=biShiftRight(c,1);if(0==c.digits[0]&&0==biHighIndex(c))break;a=biMultiplyMod(a,a,b)}return d}\n" +
            "function BarrettMu(a){this.modulus=biCopy(a);this.k=biHighIndex(this.modulus)+1;a=new BigInt;a.digits[2*this.k]=1;this.mu=biDivide(a,this.modulus);this.bkplus1=new BigInt;this.bkplus1.digits[this.k+1]=1;this.modulo=BarrettMu_modulo;this.multiplyMod=BarrettMu_multiplyMod;this.powMod=BarrettMu_powMod}\n" +
            "function BarrettMu_modulo(a){var c=biDivideByRadixPower(a,this.k-1),c=biMultiply(c,this.mu),c=biDivideByRadixPower(c,this.k+1);a=biModuloByRadixPower(a,this.k+1);c=biMultiply(c,this.modulus);c=biModuloByRadixPower(c,this.k+1);a=biSubtract(a,c);a.isNeg&&(a=biAdd(a,this.bkplus1));for(c=0<=biCompare(a,this.modulus);c;)a=biSubtract(a,this.modulus),c=0<=biCompare(a,this.modulus);return a}function BarrettMu_multiplyMod(a,c){var b=biMultiply(a,c);return this.modulo(b)}\n" +
            "function BarrettMu_powMod(a,c){var b=new BigInt;b.digits[0]=1;for(var d=a,e=c;;){0!=(e.digits[0]&1)&&(b=this.multiplyMod(b,d));e=biShiftRight(e,1);if(0==e.digits[0]&&0==biHighIndex(e))break;d=this.multiplyMod(d,d)}return b}function RSAKeyPair(a,c,b){this.e=biFromHex(a);this.d=biFromHex(c);this.m=biFromHex(b);this.chunkSize=2*biHighIndex(this.m);this.radix=16;this.barrett=new BarrettMu(this.m)}function getKeyPair(a,c,b){return new RSAKeyPair(a,c,b)}\n" +
            "function twoDigit(a){return(10>a?\"0\":\"\")+String(a)}function encryptedString(a,c){for(var b=[],d=c.length,e=0;e<d;)b[e]=c.charCodeAt(e),e++;for(;0!=b.length%a.chunkSize;)b[e++]=0;for(var d=b.length,g=\"\",f,h,k,e=0;e<d;e+=a.chunkSize){k=new BigInt;f=0;for(h=e;h<e+a.chunkSize;++f)k.digits[f]=b[h++],k.digits[f]+=b[h++]<<8;f=a.barrett.powMod(k,a.e);f=16==a.radix?biToHex(f):biToString(f,a.radix);g+=f+\" \"}return g.substring(0,g.length-1)}\n" +
            "function decryptedString(a,c){var b=c.split(\" \"),d=\"\",e,g,f;for(e=0;e<b.length;++e)for(g=16==a.radix?biFromHex(b[e]):biFromString(b[e],a.radix),f=a.barrett.powMod(g,a.d),g=0;g<=biHighIndex(f);++g)d+=String.fromCharCode(f.digits[g]&255,f.digits[g]>>8);0==d.charCodeAt(d.length-1)&&(d=d.substring(0,d.length-1));return d}setMaxDigits(130);\n" +
            "function encryption(a){var c=getKeyPair(\"010001\",\"\",\"00b6b7f8531b19980c66ae08e3061c6295a1dfd9406b32b202a59737818d75dea03de45d44271a1473af8062e8a4df927f031668ba0b1ec80127ff323a24cd0100bef4d524fdabef56271b93146d64589c9a988b67bc1d7a62faa6c378362cfd0a875361ddc7253aa0c0085dd5b17029e179d64294842862e6b0981ca1bde29979\");result=encryptedString(c,a)};";
}
