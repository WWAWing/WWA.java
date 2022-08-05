import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class WWA extends Applet implements Runnable {

    //数値デファイン
    final int ATR_0 = 0;
    final int ATR_CROP1 = 1;
    final int ATR_CROP2 = 2;
    final int ATR_TYPE = 3;
    final int ATR_MODE = 4;
    final int ATR_STRING = 5;
    final int ATR_X = 6;
    final int ATR_Y = 7;
    final int ATR_X2 = 8;
    final int ATR_Y2 = 9;
    final int ATR_ENERGY = 10;
    final int ATR_STRENGTH = 11;
    final int ATR_DEFENCE = 12;
    final int ATR_GOLD = 13;
    final int ATR_ITEM = 14;
    final int ATR_NUMBER = 15;
    final int ATR_JUMP_X = 16;
    final int ATR_JUMP_Y = 17;
    final int ATR_SOUND = 19;
    final int ATR_MOVE = 16;

    final int MAP_STREET = 0;
    final int MAP_WALL = 1;
    final int MAP_LOCALGATE = 2;
    final int MAP_URLGATE = 4;

    final int OBJECT_NORMAL = 0;
    final int OBJECT_MESSAGE = 1;
    final int OBJECT_URLGATE = 2;
    final int OBJECT_STATUS = 3;
    final int OBJECT_ITEM = 4;
    final int OBJECT_DOOR = 5;
    final int OBJECT_MONSTER = 6;
    final int OBJECT_SCORE = 11;
    final int OBJECT_SELL = 14;
    final int OBJECT_BUY = 15;
    final int OBJECT_RANDOM = 16;
    final int OBJECT_SELECT = 17;
    final int OBJECT_LOCALGATE = 18;

    final int CONFIG_QLOAD_X = 40*11+10;
    final int CONFIG_QLOAD_Y = 325-1;
    final int CONFIG_QSAVE_X = 40*11+10;
    final int CONFIG_QSAVE_Y = 360-1;
    final int CONFIG_RESTART_X = 40*11+9;
    final int CONFIG_RESTART_Y = 395-1;

    final int YESNO_NONE = 0;
    final int YESNO_YES = 1;
    final int YESNO_NO = 2;
    final int YESNO_WWAH = 3;
    final int YESNO_URLGATE = 4;
    final int YESNO_RESTART = 5;
    final int YESNO_SELL = 6;
    final int YESNO_BUY = 7;
    final int YESNO_SOUND = 8;
    final int YESNO_USEITEM = 9;
    final int YESNO_QSAVE = 10;
    final int YESNO_QLOAD = 11;
    final int YESNO_SELECT = 12;
    final int YESNO_TEXTSAVE = 13;
    final int YESNO_TEXTLOAD = 14;

    final int FRAME_ENERGY_X = 40*11+6;
    final int FRAME_ENERGY_Y = 0;
    final int FRAME_STRENGTH_X = 40*11+6;
    final int FRAME_STRENGTH_Y = 35;
    final int FRAME_DEFENCE_X = 40*11+6;
    final int FRAME_DEFENCE_Y = 35*2;
    final int FRAME_GOLD_X = 40*11+6;
    final int FRAME_GOLD_Y = 35*3;

    //データ読み込み用
    final int DATA_CHECK = 0;
    final int DATA_MAP_VERSION = 2;

    final int DATA_STATUS_ENERGY = 10;
    final int DATA_STATUS_STRENGTH = 12;
    final int DATA_STATUS_DEFENCE = 14;
    final int DATA_STATUS_GOLD = 16;
    final int DATA_STATUS_ENERGYMAX = 32;

    int DATA_MAP_COUNT;
    int DATA_OBJECT_COUNT;
    int DATA_CHARA_X;
    int DATA_CHARA_Y;
    int DATA_OVER_X;
    int DATA_OVER_Y;
    int DATA_ITEM;

    final int EX_DATA_CHARA_X = 38;
    final int EX_DATA_CHARA_Y = 40;
    final int EX_DATA_OVER_X = 42;
    final int EX_DATA_OVER_Y = 44;
    final int DATA_IMG_CHARA_CROP = 46;
    final int DATA_IMG_YESNO_CROP = 48;
    final int EX_DATA_ITEM = 66;

    final int DATA_MAP_SIZE = 46;
    final int DATA_MES_NUMBER = 48;
    final int DATA_CHECK_PARTS = 50;
    final int DATA_SAVE_STOP = 52;
    final int EX_DATA_STATUS_ENERGYMAX = 54;
    final int DATA_FLAG_DEFAULT = 56;
    final int DATA_FLAG_OLDMAP = 58;
    final int DATA_STEP = 60;
    final int DATA_MUSIC = 62;
    final int DATA_CROP = 90;
    final int DATA_FLAG_DELP = 106;
    final int DATA_IMG_CLICK = 108;
    final int DATA_EFFECT = 110;
    final int DATA_ANMITEM = 120;
    final int DATA_VALIABLE = 140;

    final int FREAD_BLOCK = 10000;
    final int MEM_BLOCK = 65000;
    final int SOUND_MAX = 100;

    int MAP_ATR_MAX;
    int OBJECT_ATR_MAX;

    //キャラクタ位置
    int charaX, charaY;
    int movePlus = 1;		//移動単位

    //メッセージ用バッファ
    String mapcgName;
    String mojicgName;
    String worldPassword;
    int worldPassNumber;
    String worldName;

    //メディアトラッカー
    MediaTracker tracker = new MediaTracker( this );
    //各種イメージ
    Image imgMap;
    //イメージ切り取り用
    Image imgCrop[];
    int cropID;
    int cropIDchara = 4;
    //サウンド関係
    AudioClip audio[] = new AudioClip[SOUND_MAX];
    boolean soundFlag = false;

    //オフスクリーン用イメージ
    Image imgBuff;
    Image imgBuffMap;
    Image imgBuffStatus;
    Image imgBuffCharaX;
    Image imgBuffCharaY;
    Image imgBuffBattle;
    Image imgBuffButton;
    //オフスクリーン用
    Graphics gBuff;
    Graphics gBuffMap;
    Graphics gBuffStatus;
    Graphics gBuffCharaX;
    Graphics gBuffCharaY;
    Graphics gBuffBattle;
    Graphics gBuffButton;

    //ＹＥＳ，ＮＯの選択
    int yesnoNumber;
    int yesnoJudge;
    int yesnoX;
    int yesnoY;
    boolean yesnoFlag;

    //各種フラグ
    boolean loadingFlag = false;
    boolean scoreFlag;
    boolean urlJumpFlag;	//URLリンク時の低速モード
    boolean g_bUseUrlJump;
    boolean quickSaveFlag = false;
    boolean displayMonsterFlag = false;
    boolean gFileNotFound;
    boolean gImageNotFound;
    boolean gDataBroken;
    boolean g_bIOError = false;
    boolean g_bAudioError = false;
    boolean bDispUseItemMes = true;
    boolean g_bAttackJudgeMes = true;	//攻撃不可の処理を行うか
    boolean g_bDisplayCenter = false;
    boolean g_bJREFont = false;
    boolean g_bRefuseExtend = false;

    //キー入力
    boolean UpKey, DownKey, LeftKey, RightKey;
    int currentKey;
    boolean inputKey = true;

    //タイマの感覚
    Thread timer;
    int countAnimation;
    int countRepaint;
    int waitCounterLast = 0;
    int g_iRepaintCount = 10;
    int g_iRepaintRetry = 0;

    //マップ位置
    int mdata;
    int mdataBuff;
    int XpBuff;
    int YpBuff;

    //マップ書き換え用フラグ
    boolean mapFlag[][] = new boolean[11][11];
    boolean mapFlagAll;
    boolean mapFlagErase;
    boolean flagDisplayStatus;

    int movingSkip;		//移動処理をスキップする回数
    boolean repaintSkip;
    int itemboxBuff = -1;
    int g_iUseItem = 0;

    //ステータス
    int itemStrength;
    int itemDefence;
    long score;

    //各種上限、サイズ
    int g_iMapWidth = 101;
    int g_iCropMax;
    int g_iMapPartsMax;
    int g_iObjectPartsMax;
    int g_iMesNumberMax;

    //モンスター攻撃用
    int monsterEnergy;
    int monsterStrength;
    int monsterDefence;
    int monsterGold;
    int attackXp;
    int attackYp;
    boolean attackFlag;
    boolean attackFlagTurn;
    int g_iAttackTurn;

    //描画モード
    boolean g_bFadeBlack = false;
    boolean g_bRestPlayer = true;
    boolean g_bMapFlagMove = false;

    int g_iMapObjMove[][][] = new int[13][13][2];	//移動アニメ用フラグ
    int g_iTurnSkip = 0;	//ターンスキップ
    boolean g_bPopup = false;
    int g_iRestCount = 0;	//無入力の時間

    //ファイル入出力
    byte byteBuff[]; // = new byte[MEM_BLOCK];
    byte byteBuffPress[] = new byte[MEM_BLOCK];

    //クイックセーブ用
    int QSaveParameter[] = new int[200];
    short QSaveMap[][];
    short QSaveMapObject[][];
    int QSaveObjectAttribute[][];
    int QSaveMapAttribute[][];
    //データ保存用
    byte PressData[];
    int g_bOldMap = 0;

    //作業用
    int g_iYesnoY;
    int g_iPointer;
    int g_iBlockByteBuff = 1;
    int g_iBlockByteBuffPress = 1;
    int g_iFileSize = 0;
    int g_iPointerExtract;
    boolean g_bCompleteExtract;
    int g_iPageNumber = 0;	//改ページ用
    int g_iLast;	//トークンポインタ
    Random random = new Random();	//ランダム

    //エフェクト用
    int g_iEffWait = 0;
    int g_iEffCrop[] = new int[4];
    int g_iEffCurrent = 0;
    int g_iAnmItemBox;
    int g_iAnmItemOld;
    int g_iAnmItemCount = 0;
    int g_iAnmItemX;
    int g_iAnmItemY;
    int g_iAnmItemCountAdd = 0;
    int g_iChangeStatus[] = new int[4];

    //V3.1追加分
    boolean g_bSkipPaint = false;	//扉アニメーション補正用

    ////static変数
//各アイコンのクロップ番号
    int CROP_YES = 13;
    int CROP_NO = 14;
    int CROP_YES2 = 15;
    int CROP_NO2 = 16;
    int CROP_ENERGY = 23;
    int CROP_STRENGTH = 24;
    int CROP_DEFENCE = 25;
    int CROP_GOLD = 26;
    int CROP_BOM = 33;
    int CROP_STFRAME = 34;
    int CROP_MAINFRAME = 10;
    int CROP_ITEMFRAME = 21;
    //キャラクタ位置
    int playerX, playerY;
    char moveDirect = 2;	//移動方向
    //メッセージ用バッファ
    String strMessage[];
    String g_szMessageSystem[]  = new String[20];
    int strNumber;
    //画像位置
    int g_iImgCharaCrop = 2;
    int g_iImgClickItem = 0;
    //マップ用バッファ
    int mapX, mapY;
    short map[][];
    short mapObject[][];
    //背景、物体キャラクタデータ
    int objectAttribute[][];
    int mapAttribute[][];

    int statusEnergy;
    int statusEnergyMax;
    int statusStrength;
    int statusDefence;
    int statusGold;
    int itemBox[] = new int[12];
    int g_iValiable[] = new int[100];
    int gameoverXp;	//ゲームオーバー
    int gameoverYp;

    int TimerCount = 20;
    boolean g_bPaintMapMove = true;
    int g_iStep = 0;
    int g_iMusicNumber = 0;

    boolean messageFlag;
    boolean configFlag;
    boolean g_bMapFlagAllB;
    boolean g_bJump = false;
    boolean g_bStopInput = false;
    String g_szCopyRight;
    int g_bSaveStop = 0;
    int g_bDefault = 0;
    int g_bAnmItem = 1;

    boolean g_bNoExec = false;
    int g_iXpoint, g_iYpoint;
    boolean g_bReturnMessage = false;
    String g_szReturnMessage;
    // int g_iOldPartsObject;	//重複判定用
    boolean g_bPlayAudio = false;
    int g_bDelPlayer = 0;
    boolean g_bGameOver = false;



//////////////////////////////////////////////////
//システム初期化

    public void init()
    {
        soundFlag = false;
        attackFlag = false;
        scoreFlag = false;
        urlJumpFlag = false;

        //各種データ初期化
        inputKey = true;
        yesnoNumber = YESNO_NONE;
        yesnoJudge = YESNO_NONE;
        mapFlagErase = false;
        messageFlag = false;
        configFlag = false;
        UpKey = false;
        DownKey = false;
        LeftKey = false;
        RightKey = false;
        countRepaint = 0;
        movingSkip = 0;
        repaintSkip = false;
        currentKey = 0;
        mapFlagAll = true;
        gFileNotFound = false;
        gImageNotFound = false;
        gDataBroken = false;
        PressData = null;
        timer = null;

        //オフスクリーン作成
        imgBuff = createImage( 40*11, 40*11 );
        gBuff = imgBuff.getGraphics();
        imgBuffStatus = createImage( 40*3, 40*11 );
        gBuffStatus = imgBuffStatus.getGraphics();
        imgBuffMap = createImage( 40, 40 );
        gBuffMap = imgBuffMap.getGraphics();
        imgBuffCharaX = createImage( 40*2, 40 );
        gBuffCharaX = imgBuffCharaX.getGraphics();
        imgBuffCharaY = createImage( 40, 40*2 );
        gBuffCharaY = imgBuffCharaY.getGraphics();
        imgBuffBattle = createImage( 340, 60 );
        gBuffBattle = imgBuffBattle.getGraphics();
        imgBuffButton = createImage( 120, 35 );
        gBuffButton = imgBuffButton.getGraphics();

        //フォントサイズからフォントタイプ判定
        judgeFontAttribute();

        System.out.println( "<init>" );
    }


    public void start()
    {
        //タイマ始動
        if( timer == null ){
            timer = new Thread(this);
            timer.start();
        }
        System.out.println( "<start>" );
    }


    public void stop()
    {
        System.out.println( "<stop>" );
    }


    public void destroy()
    {
        int i;

        //背景音楽停止
        playAudio( 99 );
        //タイマ停止
        if( timer != null ) timer.stop();
        //timer = null;
        //オフスクリーン破棄
        gBuff.dispose();
        gBuffStatus.dispose();
        gBuffMap.dispose();
        gBuffCharaX.dispose();
        gBuffCharaY.dispose();
        gBuffBattle.dispose();
        //画像解放
        for( i = 0 ; i < g_iCropMax ; ++i ) imgCrop[i].flush();
        //メモリ解放
        System.gc();

        System.out.println( "<destroy>" );
    }



//////////////////////////////////////////////////
//タイマ処理

    public void run()
    {
        while( timer.isAlive() ){
            try {
                if( urlJumpFlag == true ) Thread.sleep( 500 );
                if( (yesnoNumber == YESNO_SOUND) || (loadingFlag == false) ) Thread.sleep( 200 );
                else Thread.sleep( TimerCount );
            } catch( InterruptedException e ){
                System.err.println("111 Thread Error!");
            }
            //再描画
            if( repaintSkip == false ) repaint();
        }
    }



//////////////////////////////////////////////////
//描画処理

    public void update( Graphics g )
    {
        paint(g);
    }

    public void paint( Graphics g )
    {
        //変数定義
        int i, j;
        int x, y;
        int number;

        //エラーチェック
        if( ErrorCheck(g) == true ) return;

        //データを読み込んでいない場合
        if( loadingFlag == false ){
            //マップデータ読み込み
            LoadingMessage( g, 0 );
            loadMapData( g, getParameter("paramMapName"), true );
            if( (gFileNotFound == true) || (g_bIOError == true) ) return;
            //画像読み込み
            LoadingMessage( g, 1 );
            graphicLoading( g );
            if( gImageNotFound == true ) return;
            twait( 500 );
            //サウンドデータ
            yesnoNumber = YESNO_SOUND;
            yesnoFlag = true;
            //gBuffX.setColor( Color.white );
            //gBuffX.fillRect( 0, 0, 560, 440 );
            loadingFlag = true;
        }

        //チェックボックスの確認
        if( (g_cbSavePassword.getState() == true) || (g_cbLoadPassword.getState() == true) ){
            if( g_bOpenLoadPass == true ) ExtractLoadMapData();
            g_frameWin.dispose();
            g_cbSavePassword.setState( false );
            g_cbLoadPassword.setState( false );
            g_bOpenFrameWin = false;
            urlJumpFlag = false;
            mapFlagAll = true;
            mapFlagErase = false;
            g_iRepaintRetry = 3;
        }
        //モンスターの能力値表示
        if( displayMonsterFlag == true ){
            DisplayMonsterData( g );
        }
        //キー入力待ちならばバックバッファから書き換え
        if( inputKey == false ){
            waitCounterLast = 200;
            g.drawImage( imgBuff, 0, 0, this );
            g.drawImage( imgBuffStatus, 40*11, 0, this );
            return;
        }
        //ＹＥＳ，ＮＯの判定
        if( yesnoJudge(g) == false ) return;
        if( yesnoFlag == true ) return;

        //ステータス表示フラグ
        if( flagDisplayStatus == true ){
            displayConfigWindow( g, true, true, true, true );
            flagDisplayStatus = false;
        }
        //攻撃状態の場合
        if( attackFlag == true ){
            attackMonster( g, attackXp, attackYp );
            return;
        }
        //キャラクタ移動
        if( (mapFlagAll != true) && (inputKey != false) && (attackFlag != true) ){
            if( movingSkip != 0 ) --movingSkip;
            else moveCharacter(g);
        }
        //ゲームオーバー
        if( g_bGameOver == true ) GameOver( g );
        //位置変更
        if( g_bJump == true ) JumpPoint( playerX, playerY );
        //曲変更
        if( g_bPlayAudio == true ) playAudio( g_iMusicNumber );
        //プレーヤーの現在位置取得
        playerX = charaX /5 +mapX *10;
        playerY = charaY /5 +mapY *10;

        if( g_iRepaintRetry > 0 ){
            mapFlagAll = true;
            mapFlagErase = false;
            --g_iRepaintRetry;
        }
        //マップ全体書き換え
        if( mapFlagAll == true ){
            repaintSkip = true;
            paintMapAll( g, true );
            repaintSkip = false;
            movingSkip = 3;
        }
        if( g_bMapFlagAllB == true ){
            g_bMapFlagAllB = false;
            paintMapAll( g, true );
        }
        //定義ウィンドウの描画
        if( configFlag == true ){
            displayConfigWindow( g, true, true, true, true );
            arrangeItem( 0 );
            configFlag = false;
        }
        //マップとキャラクタの描画
        paintMap( g );
        //マップ部分書き換えのフラグセット
        setMapFlag();
        g_bSkipPaint = false;

        //ウェイト
        if( waitCounterLast != 0 ){
            twait( waitCounterLast );
            waitCounterLast = 0;
        }
        //日本語の表示
        if( (messageFlag == false) && (g_bReturnMessage == true) ){
            messageFlag = true;
            g_bReturnMessage = false;
            strNumber = 1;
            //g_iOldPartsObject = 0;
        }
        if( messageFlag == true ) MessageCheck( g );

        //スコアの表示
        if( scoreFlag == true ){
            displayScore( gBuff );
            scoreFlag = false;
        }
        //押しボタン描画クリア
        if( (g_iUseItem > 0) && (inputKey != false) ){
            g_iUseItem = 0;
            itemboxBuff = -1;
            twait( 100 );
            displayConfigWindow( g, true, false, true, false );
        }
        for( i = 0 ; i < 4 ; ++i ){
            if( (g_iChangeStatus[i] == 1) && (g_bAnmItem == 1) ) configFlag = true;
            if( g_iChangeStatus[i] > 0 ) --g_iChangeStatus[i];
        }
    }


    //メッセージチェック
    public void MessageCheck( Graphics g )
    {
        messageFlag = false;
        if( g_iPageNumber == -1 ) g_iPageNumber = 0;

        if( !((strMessage[strNumber].equals("")) || (strNumber == 0)) ){
            int Ytop;
            if( scoreFlag == true ) Ytop = 80;
            else if( g_bDisplayCenter == true ) Ytop = -3;
            else if( charaY /5 >= 6 ) Ytop = -2;
            else Ytop = -1;

            //定期描き換え用
            if( drawJapaneseFrame( gBuff, g, strMessage[strNumber], 50, Ytop ) == false ) return;

            inputKey = false;
            mapFlagAll = true;
            mapFlagErase = false;
            movingSkip = 3;
        }
        g_bDisplayCenter = false;
    }


    //読み込み中のメッセージ
    public void LoadingMessage( Graphics g, int Number )
    {
        Font fon;

        g.setColor(Color.white);
        g.fillRect( 0, 0, 40*14, 40*11 );

        g.setColor(Color.black);
        fon = new Font("TimesRoman", Font.PLAIN, 32 );
        g.setFont(fon);
        g.drawString( "Welcome to WWA!" ,100,70 );

        fon = new Font("TimesRoman", Font.PLAIN, 18 );
        g.setFont(fon);
        g.drawString( "(C)1996-2015  NAO   Ver 3.11" ,160, 390 );

        fon = new Font("TimesRoman", Font.PLAIN, 22 );
        g.setFont(fon);

        if( Number >= 0 ){
            g.drawString( "Now Map data Loading ....." ,50,140 );
        }
        if( Number >= 1 ){
            g.drawString( "Now Map data Loading ..... Complete!" ,50,140 );
            g.drawString( "Now CG data Loading ....." ,50,170 );
        }
        if( Number >= 3 ){
            g.drawString( "Now CG data Loading ..... Complete!" ,50,170 );
            g.drawString( "Now Making chara CG ....." ,50,200 );
        }
        if( Number >= 4 ){
            g.drawString( "Now Making chara CG ..... Complete!" ,50,200 );
        }
        if( Number >= 1 ){
            //ワールド名表示
            fon = new Font("TimesRoman", Font.PLAIN, 18 );
            g.setFont(fon);
            g.drawString( "World Name   " +worldName, 160 ,360 );
        }
    }



//////////////////////////////////////////////////
//文字列を数値変換

    public int parse( String str )
    {
        int number = 0;

        try{
            number = Integer.parseInt( str );
        } catch( NumberFormatException e ){};

        return number;
    }



//////////////////////////////////////////////////
//エラーチェック

    public boolean ErrorCheck( Graphics g )
    {
        if( (gFileNotFound == true) || (gImageNotFound == true) || (gDataBroken == true) || (g_bIOError == true) || (g_bRefuseExtend == true) ){
            g.setColor( Color.white );
            g.fillRect( 0, 0, 560, 440 );
            g.setColor(Color.black);
            Font fon = new Font("TimesRoman", Font.PLAIN, 16 );
            g.setFont(fon);
            if( gDataBroken == true ){
                g.drawString( "マップデータが壊れています。" ,10,180 );
                g.drawString( "テキストモードで送信していないか確認してください。" ,10,210 );
            } else if( gFileNotFound == true ){
                g.drawString( "マップデータファイル " +getParameter("paramMapName") +" が見つかりません。" ,10,180 );
                g.drawString( "データがアップロードされているか確認してください。" ,10,210 );
            } else if( gImageNotFound == true ){
                if( mapcgName.equals("") ){
                    g.drawString( "画像データファイル名が見つかりません。" ,10,180 );
                    g.drawString( "マップデータが壊れています。" ,10,210 );
                } else {
                    g.drawString( "画像データファイル " +mapcgName +" が見つからないかアクセスできません。" ,10,180 );
                    g.drawString( "データがアップロードされているか、" ,10,210 );
                    g.drawString( "パーミッションが読み込み可になっているかなどを確認してください。" ,10,240 );
                }
            } else if( g_bIOError == true ){
                g.drawString( "マップデータにアクセスできません。" ,10,180 );
                g.drawString( "パーミッションが読み込み可になっているかなどを確認してください。" ,10,210 );
            } else if( g_bRefuseExtend == true ){
                g.drawString( "古いマップや暗証番号が４桁以上のマップには拡張マクロは使えません。" ,10,180 );
            }
            twait( 1000 );
            return true;
        }
        return false;
    }



//////////////////////////////////////////////////
//Ｙｅｓ，Ｎｏ判定

    public void yesnoJudgeSub( Graphics g, Graphics gGlobal, String str, int x, int y, int yx )
    {
        //中央付き
        if( y == 0 ) y = -3;

        if( yesnoFlag == true ){
            yesnoX = yx;
            if( yesnoNumber == YESNO_SOUND ){
                gBuff.setColor( Color.white );
                gBuff.fillRect( 0, 0, 440, 440 );
                gBuffStatus.setColor( Color.white );
                gBuffStatus.fillRect( 0, 0, 120, 440 );
                drawJapaneseFrame( gBuff, null, str, x, y );
            } else {
                drawJapaneseFrame( g, gGlobal, str, x, y );
            }
            yesnoY = g_iYesnoY;
            yesnoFlag = false;
        }
    }


    public void playAudio( int number )
    {
        if( g_iMusicNumber == number ) return;

        //背景音楽停止
        if( (number == 99) && (g_iMusicNumber != 0) && (audio[g_iMusicNumber] != null) && (soundFlag == true) ){
            audio[g_iMusicNumber].stop();
            g_iMusicNumber = 0;
            return;
        }
        //ロード時の音楽再開
        if( (number == 100) && (g_iMusicNumber != 0) && (audio[g_iMusicNumber] != null) && (soundFlag == true) ){
            audio[g_iMusicNumber].loop();
        }
        //演奏
        if( (number != 0) && (number < SOUND_MAX) && (audio[number] != null) && (soundFlag == true) ){
            if( number < 70 ){
                audio[number].play();
            } else {
                //背景音楽
                if( (g_iMusicNumber != 0) && (audio[g_iMusicNumber] != null) ) audio[g_iMusicNumber].stop();
                g_iMusicNumber = number;
                audio[g_iMusicNumber].loop();
            }
        }
    }


    public void getAudioClipSub( int number )
    {
        if( number < SOUND_MAX ){
            try {
                audio[number] = getAudioClip( getDocumentBase(), number+".au" );
            } catch( Exception e ){
                System.err.println( "150 Audio Error!" );
                g_bAudioError = true;
            }
        }
    }


    public void SaveStopMes()
    {
        DisplayMessage( "ここではセーブ機能は\n使用できません。", true );
        yesnoNumber = YESNO_NONE;
        yesnoJudge = YESNO_NONE;
        yesnoFlag = false;
    }


    public boolean yesnoJudge( Graphics g )
    {
        //変数定義
        int i, j;
        int x, y;
        int number;
        int Ytop;
        if( charaY /5 >= 6 ) Ytop = -2;
        else Ytop = -1;
        int iBoxPos;

        //アイテム使用メッセージの表示可否
        if( (yesnoNumber == YESNO_USEITEM) && (strMessage[8].equals("BLANK")) ){
            UseItem( g );
            yesnoNumber = YESNO_NONE;
            yesnoFlag = false;
        }

        if( yesnoNumber != YESNO_NONE ){
            //ボタン表示
            if( yesnoFlag == false ){
                gBuffMap.setColor( Color.white );
                gBuffMap.fillRect( 0, 0, 40, 40 );
                if( yesnoJudge == YESNO_YES ){
                    gBuffMap.drawImage( imgCrop[CROP_YES2], 0, 0, this );
                    gBuff.drawImage( imgBuffMap, yesnoX+2, yesnoY+2, this );
                }
                gBuffMap.setColor( Color.white );
                gBuffMap.fillRect( 0, 0, 40, 40 );
                if( yesnoJudge == YESNO_NO ){
                    gBuffMap.drawImage( imgCrop[CROP_NO2], 0, 0, this );
                    gBuff.drawImage( imgBuffMap, yesnoX+2 +40, yesnoY+2, this );
                }
                g.drawImage( imgBuff, 0, 0, this );
                g.drawImage( imgBuffStatus, 40*11, 0, this );
                twait( 100 );

                //サウンド
                if( (yesnoJudge == YESNO_YES) || (yesnoJudge == YESNO_NO) ){
                    playAudio( 1 );
                }
            }
            //ＷＷＡサイトにジャンプ
            if( yesnoNumber == YESNO_WWAH ){
                yesnoJudgeSub( gBuff, g, "ＷＷＡの公式サイトを開きますか？", 50, 0, 40*7 +16 );

                if( yesnoJudge == YESNO_YES ){
                    try{
                        urlJumpFlag = true;
                        URL url = new URL( "http://www.wwajp.com/" );
                        if( g_bPopup == true ) getAppletContext().showDocument( url, "new" );
                        else getAppletContext().showDocument( url );
                    } catch( MalformedURLException e ){
                        System.err.println( "141 URL Error!" );
                    }
                }
            }
            //ＵＲＬリンク
            if( yesnoNumber == YESNO_URLGATE ){
                if( strMessage[5].equals("") ) yesnoJudgeSub( gBuff, g, "他のページにリンクします。\nよろしいですか？", 50, 0, 40*7 +16 );
                else yesnoJudgeSub( gBuff, g, strMessage[5], 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ) jumpURL( strMessage[strNumber], false );
            }
            //再スタート
            if( yesnoNumber == YESNO_RESTART ){
                yesnoJudgeSub( gBuff, g, "初めからスタートしなおしますか？", 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ){
                    loadMapData( g, "", false );
                    //moveDirect = 2;
                    g_iImgCharaCrop = 2;
                    SetDirectPlayer( 2 );
                    SetYesNoCrop( 13 );
                    g_bSaveStop = 0;
                    g_bDefault = 0;
                    g_bOldMap = 0;
                    g_iStep = 0;
                    g_iEffWait = 0;
                    g_iImgClickItem = 0;
                    g_bAnmItem = 1;
                    g_bFadeBlack = true;
                    //メッセージクリア
                    messageFlag = false;
                    g_iPageNumber = 0;
                    //背景音楽停止
                    playAudio( 99 );
                    g_bRestPlayer = true;
                }
            }
            //物を売るキャラクタ
            if( yesnoNumber == YESNO_SELL ){
                yesnoJudgeSub( gBuff, g, strMessage[strNumber], 50, Ytop, 40*7 +16 );

                if( yesnoJudge == YESNO_YES ){
                    int item = objectAttribute[mdataBuff][ATR_ITEM];

                    for( i = 0 ; i < 12 ; ++i ){
                        if( itemBox[i] == 0 ) break;
                    }
                    iBoxPos = objectAttribute[item][ATR_NUMBER];
                    if( (i == 12) && (item != 0) && ((iBoxPos == 0) || (objectAttribute[itemBox[iBoxPos-1]][ATR_NUMBER] == 0)) ){
                        messageFlag = true;
                        strNumber = 1;
                        if( g_szMessageSystem[1].equals("") ) strMessage[strNumber] = "これ以上、アイテムを持てません。";
                        else strMessage[strNumber] = g_szMessageSystem[1];
                    } else {
                        if( statusGold >= objectAttribute[mdataBuff][ATR_GOLD] ){
                            statusGold -= objectAttribute[mdataBuff][ATR_GOLD];
                            if( objectAttribute[mdataBuff][ATR_ENERGY] > 30000 ){
                                statusEnergy -= (objectAttribute[mdataBuff][ATR_ENERGY] -30000);
                                if( (statusEnergy <= 0) && (objectAttribute[mdataBuff][ATR_ENERGY] != 0) ){
                                    GameOver( g );
                                    yesnoNumber = YESNO_NONE;
                                    yesnoJudge = YESNO_NONE;
                                    return false;
                                }
                            } else {
                                statusEnergy += objectAttribute[mdataBuff][ATR_ENERGY];
                            }
                            statusStrength += objectAttribute[mdataBuff][ATR_STRENGTH];
                            statusDefence += objectAttribute[mdataBuff][ATR_DEFENCE];

                            if( objectAttribute[mdataBuff][ATR_ENERGY] != 0 ) g_iChangeStatus[0] = 20;
                            if( objectAttribute[mdataBuff][ATR_STRENGTH] != 0 ) g_iChangeStatus[1] = 20;
                            if( objectAttribute[mdataBuff][ATR_DEFENCE] != 0 ) g_iChangeStatus[2] = 20;
                            if( objectAttribute[mdataBuff][ATR_GOLD] != 0 ) g_iChangeStatus[3] = 20;
                            if( objectAttribute[objectAttribute[mdataBuff][ATR_ITEM]][ATR_STRENGTH] != 0 ) g_iChangeStatus[1] = 20;
                            if( objectAttribute[objectAttribute[mdataBuff][ATR_ITEM]][ATR_DEFENCE] != 0 ) g_iChangeStatus[2] = 20;

                            //アイテムアニメ用
                            if( item != 0 ) AnmItemSub( i, iBoxPos, XpBuff, YpBuff );
                            //アイテム整理
                            arrangeItem( objectAttribute[mdataBuff][ATR_ITEM] );
                            displayConfigWindow( g, true, true, true, false );
                            //キャラクタ出現
                            mapFlagAll = true;
                            appearChara( g, XpBuff, YpBuff, 0, mdataBuff );
                        } else {
                            if( strMessage[6].equals("") ){
                                messageFlag = true;
                                strNumber = 1;
                                strMessage[strNumber] = "所持金がたりない。";
                            } else if( !strMessage[6].equals("BLANK") ){
                                messageFlag = true;
                                strNumber = 6;
                            }
                        }
                    }
                }
            }
            //物を買うキャラクタ
            if( yesnoNumber == YESNO_BUY ){
                yesnoJudgeSub( gBuff, g, strMessage[strNumber], 50, Ytop, 40*7 +16 );

                if( yesnoJudge == YESNO_YES ){
                    for( i = 0 ; i < 12 ; ++i ){
                        if( objectAttribute[mdataBuff][ATR_ITEM] == itemBox[i] ){
                            statusGold += objectAttribute[mdataBuff][ATR_GOLD];
                            if( objectAttribute[mdataBuff][ATR_GOLD] != 0 ) g_iChangeStatus[3] = 20;
                            itemBox[i] = 0;
                            arrangeItem( 0 );
                            displayConfigWindow( g, true, true, true, false );
                            //キャラクタ出現
                            //mapFlagErase = true;
                            mapFlagAll = true;
                            appearChara( g, XpBuff, YpBuff, 0, mdataBuff );
                            break;
                        }
                    }
                    if( i == 12 ){
                        if( strMessage[7].equals("") ){
                            messageFlag = true;
                            strNumber = 1;
                            strMessage[strNumber] = "アイテムを持っていない。";
                        } else if( !strMessage[7].equals("BLANK") ){
                            messageFlag = true;
                            strNumber = 7;
                        }
                    }
                }
            }
            //二者択一
            if( yesnoNumber == YESNO_SELECT ){
                yesnoJudgeSub( gBuff, g, strMessage[strNumber], 50, Ytop, 40*7 +16 );

                if( yesnoJudge == YESNO_YES ){
                    //キャラクタ出現
                    mapFlagAll = true;
                    appearChara( g, XpBuff, YpBuff, 3, mdataBuff );
                } else if( yesnoJudge == YESNO_NO ){
                    //キャラクタ出現
                    mapFlagAll = true;
                    appearChara( g, XpBuff, YpBuff, 4, mdataBuff );
                }
            }
            //効果音データのロード
            if( yesnoNumber == YESNO_SOUND ){
                int iMesY = 360;
                if( g_szMessageSystem[2].equals("") ){
                    yesnoJudgeSub( gBuff, g, "効果音データをロードしますか？", 50, 0, 40*7 +16 );
                } else if( g_szMessageSystem[2].equals("ON") || g_szMessageSystem[2].equals("on") ){
                    yesnoJudge = YESNO_YES;
                    iMesY = 230;
                    DisplayMessage( "ゲームを開始します。\n画面をクリックしてください。", true );
                } else if( (g_szMessageSystem[2].equals("OFF")) || (g_szMessageSystem[2].equals("off")) ){
                    yesnoJudge = YESNO_NO;
                    DisplayMessage( "ゲームを開始します。\n画面をクリックしてください。", true );
                } else {
                    yesnoJudgeSub( gBuff, g, g_szMessageSystem[2], 50, 0, 40*7 +16 );
                }
                yesnoFlag = false;

                if( yesnoJudge == YESNO_YES ){
                    //読み込みメッセージ
                    g.setColor(Color.black);
                    Font fon = new Font( "TimesRoman", Font.PLAIN, 22 );
                    g.setFont( fon );
                    g.drawString( "Now Sound data Loading ....." ,50,iMesY );
                    //サウンドデータ$$
                    for( number = 1 ; number <= 3 ; ++number ){
                        if( (number == 2) ) continue;
                        getAudioClipSub( number );
                    }
                    for( i = 0 ; i < g_iMapPartsMax ; ++i ){
                        number = mapAttribute[i][ATR_SOUND];
                        if( (number != 0) && (number < 100) && (audio[number] == null) ){
                            getAudioClipSub( number );
                        }
                    }
                    for( i = 0 ; i < g_iObjectPartsMax ; ++i ){
                        number = objectAttribute[i][ATR_SOUND];
                        if( (number != 0) && (number < 100) && (audio[number] == null) && (objectAttribute[i][ATR_TYPE] != OBJECT_RANDOM) ){
                            getAudioClipSub( number );
                        }
                    }
                    soundFlag = true;
                    g.drawString( "Now Sound data Loading ..... Complete!" ,50,iMesY );
                    twait( 500 );
                } else if( yesnoJudge == YESNO_NO ){
                    soundFlag = false;
                }
            }
            //物を使う
            if( yesnoNumber == YESNO_USEITEM ){
                if( (charaX %5 == 0) && (charaY %5 == 0) ){
                    if( strMessage[8].equals("") ) yesnoJudgeSub( gBuff, g, "このアイテムを使います。\nよろしいですか？", 50, Ytop, 40*7 +16 );
                    else yesnoJudgeSub( gBuff, g, strMessage[8], 50, Ytop, 40*7 +16 );
                } else {
                    //移動中なら処理スキップ
                    yesnoNumber = YESNO_NONE;
                    yesnoJudge = YESNO_NONE;
                    yesnoFlag = false;
                }
                if( yesnoJudge == YESNO_YES ){
                    UseItem( g );
                }
            }
            //データの一時保存
            if( yesnoNumber == YESNO_QSAVE ){
                if( g_bSaveStop == 1 ){
                    SaveStopMes();
                    return true;
                }
                yesnoJudgeSub( gBuff, g, "データの一時保存をします。\nよろしいですか？\n→Ｎｏでデータ復帰用パスワードの\n　表示選択ができます。", 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ){
                    QuickSave();
                    quickSaveFlag = true;
                } else if( yesnoJudge == YESNO_NO ){
                    yesnoNumber = YESNO_TEXTSAVE;
                    yesnoJudge = YESNO_NONE;
                    yesnoFlag = true;
                    twait( 200 );
                }
            }
            //データの読み込み
            if( yesnoNumber == YESNO_QLOAD ){
                yesnoJudgeSub( gBuff, g, "データを読み込みますか？\n→Ｎｏでデータ復帰用パスワードの\n　入力選択ができます。", 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ){
                    QuickLoad();
                } else if( yesnoJudge == YESNO_NO ){
                    yesnoNumber = YESNO_TEXTLOAD;
                    yesnoJudge = YESNO_NONE;
                    yesnoFlag = true;
                    twait( 200 );
                }
            }
            //保存用テキストの表示
            if( yesnoNumber == YESNO_TEXTSAVE ){
                if( g_bSaveStop == 1 ){
                    SaveStopMes();
                    return true;
                }
                yesnoJudgeSub( gBuff, g, "データ復帰用のパスワードを\n表示しますか？", 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ) PressSaveMapData();
            }
            //保存用テキストの入力
            if( yesnoNumber == YESNO_TEXTLOAD ){
                yesnoJudgeSub( gBuff, g, "データ復帰用のパスワードを\n入力しますか？", 50, 0, 40*7 +16 );
                if( yesnoJudge == YESNO_YES ) InputLoadPassword();
            }
            //判定終了
            if( yesnoJudge != YESNO_NONE ){
                yesnoNumber = YESNO_NONE;
                yesnoJudge = YESNO_NONE;
                mapFlagAll = true;
                mapFlagErase = false;
                twait( 200 );
            }
            return false;
        }
        return true;
    }


    public void UseItem( Graphics g )
    {
        //メッセージ表示
        messageFlag = true;
        strNumber = objectAttribute[itemBox[itemboxBuff]][ATR_STRING];
        //キャラクタ出現
        mapFlagAll = true;
        appearChara( g, playerX, playerY, 2 );
        if( objectAttribute[itemBox[itemboxBuff]][ATR_MODE] == 1 ) itemBox[itemboxBuff] = 0;
    }



//////////////////////////////////////////////////
//マップの全体書き換え

    public void paintMapAll( Graphics g, boolean bRepaint )
    {
        //変数定義
        int i, j, k;
        int x, y;

        //全体書き換えフラグ初期化
        mapFlagAll = false;

        //移動アニメフラグ初期化
        for( j = 0 ; j < 13 ; ++j ){
            for( i = 0 ; i < 13 ; ++i ){
                for( k = 0 ; k < 2 ; ++k ){
                    g_iMapObjMove[j][i][k] = 0;
                }
            }
        }
        //画面フェード処理
        if( g_bFadeBlack == true ){
            g_bFadeBlack = false;
            g.setColor( Color.gray );
            for( k = 0 ; k < 40*5+20 ; ++k ){
                g.drawRect( k, k, 40 *11 -k*2, 40*11 -k*2 );
                if( k %10 == 0 ) twait( 20 );
            }
            twait( 20 );
            g.fillRect( 0, 0, 440, 440 );
            mapFlagErase = true;
        }
        //次のマップを表示
        if( (mapFlagErase == true) && (bRepaint == true) ){
            mapFlagErase = false;
            SetDirectPlayer( 0 );
            //スクロール表示
            if( (g_bPaintMapMove == true) && (g_bMapFlagMove == true) ){
                g_bMapFlagMove = false;
                x = 0;
                y = 0;
                for( k = 0 ; k < 11 ; ++k ){
                    if( moveDirect == 4 ) x = 10 -k;
                    else if( moveDirect == 6 ) x = k -10;
                    else if( moveDirect == 2 ) y = k -10;
                    else if( moveDirect == 8 ) y = 10 -k;
                    paintMapMove( g, bRepaint, x, y );
                    twait( 20 );
                }
            }
            //幕下ろし表示
            else {
                for( j = 0 ; j < 11 ; ++j ){
                    for( i = 0 ; i < 11 ; ++i ){
                        displayCharacter( g, (i +mapX *10), (j +mapY *10) );
                    }
                    twait( 20 );
                }
            }
        }
        //画面描画
        paintMapMove( g, bRepaint, 0, 0 );
        //定義ウィンドウの描画
        displayConfigWindow( g, true, true, true, true );
    }



//////////////////////////////////////////////////
//移動パーツありの時の画面全体描画

    public void paintMapMove( Graphics g, boolean bRepaint, int iXtop, int iYtop )
    {
        //変数定義
        int i, j, k;
        int x, y;
        int iMapData;
        int mx, my;
        int iCount;
        int iCrop = 0;

        if( g_bSkipPaint == true ) return;

        //背景色で塗りつぶし
        gBuff.setColor( Color.gray );
        gBuff.fillRect( 0, 0, 440, 440 );

        //背景描画
        for( j = 0 ; j < 11 ; ++j ){
            for( i = 0 ; i < 11 ; ++i ){
                iMapData = map[j +mapY *10 +iYtop][i +mapX *10 +iXtop];
                cropID = mapAttribute[iMapData][ATR_CROP1];
                gBuff.drawImage( imgCrop[cropID], i*40, j*40, this );

            }
        }
        //プレーヤー描画
        x = charaX *8;
        y = charaY *8;
        if( iXtop != 0 ){
            if( moveDirect == 4 ) x = (10 -iXtop) *40;
            else if( moveDirect == 6 ) x = (-iXtop) *40;
        }
        if( iYtop != 0 ){
            if( moveDirect == 2 ) y = (-iYtop) *40;
            else if( moveDirect == 8 ) y = (10 -iYtop) *40;
        }
        if( g_bDelPlayer == 0 ) gBuff.drawImage( imgCrop[cropIDchara], x, y, this );

        //物体描画
        for( j = -1 ; j < 12 ; ++j ){
            for( i = -1 ; i < 12 ; ++i ){
                mx = i +mapX *10 +iXtop;
                my = j +mapY *10 +iYtop;
                if( (mx < 0) || (my < 0) || (mx >= g_iMapWidth) || (my >= g_iMapWidth) ) continue;
                iMapData = mapObject[my][mx];
                if( iMapData != 0 ){
                    if( CheckNoDrawParts(iMapData, charaX /5, i, charaY /5, j) == false ){
                        if( (countAnimation %44 < 22) || (objectAttribute[iMapData][ATR_CROP2] == 0) ) cropID = objectAttribute[iMapData][ATR_CROP1];
                        else cropID = objectAttribute[iMapData][ATR_CROP2];

                        if( g_iMapObjMove[j+1][i+1][0] > 0 ) --g_iMapObjMove[j+1][i+1][0];
                        if( g_iMapObjMove[j+1][i+1][0] < 0 ) ++g_iMapObjMove[j+1][i+1][0];
                        if( g_iMapObjMove[j+1][i+1][1] > 0 ) --g_iMapObjMove[j+1][i+1][1];
                        if( g_iMapObjMove[j+1][i+1][1] < 0 ) ++g_iMapObjMove[j+1][i+1][1];
                        x = i *40 +g_iMapObjMove[j+1][i+1][0] *8;
                        y = j *40 +g_iMapObjMove[j+1][i+1][1] *8;
                        gBuff.drawImage( imgCrop[cropID], x, y, this );
                    }
                }
            }
        }

        //フレームと特殊効果描画
        if( (g_iEffWait > 0) && (g_bPaintMapMove == true) ){
            for( i = 3 ; i > 0 ; --i ){
                if( g_iEffCrop[i] != 0 ) break;
            }
            iCrop = (g_iEffCurrent /g_iEffWait) %(i+1);
            ++g_iEffCurrent;
        }
        for( j = 0 ; j < 11 ; ++j ){
            for( i = 0 ; i < 11 ; ++i ){
                //特殊画面効果
                if( (g_iEffWait > 0) && (g_bPaintMapMove == true) ) gBuff.drawImage( imgCrop[g_iEffCrop[iCrop]], i*40, j*40, this );
                //フレーム描画
                paintFrame2( gBuff, i, j, i*40, j*40 );
            }
        }
        //アイテムアニメ描画
        if( (g_iAnmItemCount > 0) && (g_bPaintMapMove == true) ){
            if( g_iPageNumber <= 0 ) ++g_iAnmItemCount;
            if( g_iAnmItemCount < 5 ){
                iCount = 0;
            } else {
                iCount = g_iAnmItemCount -5;
                if( iCount >= (10 +g_iAnmItemCountAdd) ) g_iAnmItemCount = 0;
            }
            x = g_iAnmItemX +(440 +(g_iAnmItemBox %3) *40 -g_iAnmItemX) *iCount /(10 +g_iAnmItemCountAdd);
            y = g_iAnmItemY +(140 +(g_iAnmItemBox /3) *40 -g_iAnmItemY) *iCount /(10 +g_iAnmItemCountAdd);
            displayConfigWindow( g, true, true, true, true );
            if( itemBox[g_iAnmItemBox] != 0 ){
                g.drawImage( imgCrop[objectAttribute[itemBox[g_iAnmItemBox]][ATR_CROP1]], x, y, this );
                gBuff.drawImage( imgCrop[objectAttribute[itemBox[g_iAnmItemBox]][ATR_CROP1]], x, y, this );
            }
        }
        //連続メッセージ表示中ならスキップ
        if( (bRepaint == true) && (g_iPageNumber <= 0) ) g.drawImage( imgBuff, 0, 0, this );
    }



//////////////////////////////////////////////////
//プレーヤーキャラ方向設定

    public void SetDirectPlayer( int iDirect )
    {
        if( iDirect != 0 ) moveDirect = (char)iDirect;

        if( moveDirect == 2 ) cropIDchara = g_iImgCharaCrop +2;
        else if( moveDirect == 4 ) cropIDchara = g_iImgCharaCrop +4;
        else if( moveDirect == 6 ) cropIDchara = g_iImgCharaCrop +6;
        else cropIDchara = g_iImgCharaCrop;
    }



//////////////////////////////////////////////////
//マップとキャラクタの描画

    public void paintMap( Graphics g )
    {
        //変数定義
        int i, j;
        int x, y;
        int x2, y2;

        if( countRepaint == 0 ){
            //プレーヤーキャラ方向設定
            SetDirectPlayer( 0 );
            //キャラクタパターン選択
            if( (((charaX %10) > 4) && ((moveDirect == 4) || (moveDirect == 6))) || (((charaY %10) > 4) && ((moveDirect == 8) || (moveDirect == 2))) ){
                ++cropIDchara;
            }
        }
        //全描画モード
        if( g_bPaintMapMove == true ){
            paintMapMove( g, true, 0, 0 );
            return;
        }

        //左右への移動の場合。
        if( (moveDirect == 4) || (moveDirect == 6) ){
            gBuffCharaX.setColor( Color.gray );
            gBuffCharaX.fillRect( 0, 0, 80, 40 );

            x = charaX /5;
            y = charaY /5;
            if( (charaX %5 == 0) && (moveDirect == 6) ) --x;
            if( (charaX == 5*10) && (moveDirect == 4) ) --x;
            x2 = x;
            y2 = y;
            //背景マップ描画
            if( x >= 0 ){
                mdata = map[y +mapY *10][x +mapX *10];
                cropID = mapAttribute[mdata][ATR_CROP1];
                gBuffCharaX.drawImage( imgCrop[cropID], 0, 0, this );
            }
            ++x;
            if( (x +mapX *10) < g_iMapWidth ){
                mdata = map[y +mapY *10][x +mapX *10];
                cropID = mapAttribute[mdata][ATR_CROP1];
                gBuffCharaX.drawImage( imgCrop[cropID], 40, 0, this );
            }
            x = (charaX %5) *8;
            if( (charaX %5 == 0) && (moveDirect == 6) ) x += 40;
            if( (charaX == 5*10) && (moveDirect == 4) ) x += 40;
            y = (charaY %5) *8;
            if( g_bDelPlayer == 0 ) gBuffCharaX.drawImage( imgCrop[cropIDchara], x, y, this );

            //オブジェクト描画
            x = x2;
            y = y2;
            if( x >= 0 ){
                mdata = mapObject[y +mapY *10][x +mapX *10];
                if( mdata != 0 ){
                    if( (countAnimation %44 < 22) || (objectAttribute[mdata][ATR_CROP2] == 0) ) cropID = objectAttribute[mdata][ATR_CROP1];
                    else cropID = objectAttribute[mdata][ATR_CROP2];

                    if( CheckNoDrawParts(mdata,x,charaX /5,y,charaY /5) == false ){
                        gBuffCharaX.drawImage( imgCrop[cropID], 0, 0, this );
                    }
                }
            }
            ++x;
            if( (x +mapX *10) < g_iMapWidth ){
                mdata = mapObject[y +mapY *10][x +mapX *10];
                if( mdata != 0 ){
                    if( (countAnimation %44 < 22) || (objectAttribute[mdata][ATR_CROP2] == 0) ) cropID = objectAttribute[mdata][ATR_CROP1];
                    else cropID = objectAttribute[mdata][ATR_CROP2];

                    if( CheckNoDrawParts(mdata,x,charaX /5,y,charaY /5) == false ){
                        gBuffCharaX.drawImage( imgCrop[cropID], 40, 0, this );
                    }
                }

            }
            //フレーム描画
            if( x2 >= 0 ) paintFrame2( gBuffCharaX, x2, y2, 0, 0 );
            paintFrame2( gBuffCharaX, x2+1, y2, 40, 0 );

            x = (charaX /5) *40;
            if( (charaX %5 == 0) && (moveDirect == 6) ) x -= 40;
            if( (charaX == 5*10) && (moveDirect == 4)) x -= 40;
            y = (charaY /5) *40;
            g.drawImage( imgBuffCharaX, x, y, this );
        }
        //上下への移動の場合。
        if( (moveDirect == 8) || (moveDirect == 2) ){
            gBuffCharaY.setColor( Color.gray );
            gBuffCharaY.fillRect( 0, 0, 40, 80 );

            x = charaX /5;
            y = charaY /5;
            if( (charaY %5 == 0) && (moveDirect == 2) ) --y;
            x2 = x;
            y2 = y;
            //背景マップ描画
            if( y >= 0 ){
                mdata = map[y +mapY *10][x +mapX *10];
                cropID = mapAttribute[mdata][ATR_CROP1];
                gBuffCharaY.drawImage( imgCrop[cropID], 0, 0, this );
            }
            ++y;
            if( (y +mapY *10) < g_iMapWidth ){
                mdata = map[y +mapY *10][x +mapX *10];
                cropID = mapAttribute[mdata][ATR_CROP1];
                gBuffCharaY.drawImage( imgCrop[cropID], 0, 40, this );
            }
            x = (charaX %5) *8;
            y = (charaY %5) *8;
            if( (charaY %5 == 0) && (moveDirect == 2) ) y += 40;
            if( g_bDelPlayer == 0 ) gBuffCharaY.drawImage( imgCrop[cropIDchara], x, y, this );

            //オブジェクト描画
            x = x2;
            y = y2;
            if( y >= 0 ){
                mdata = mapObject[y +mapY *10][x +mapX *10];
                if( mdata != 0 ){
                    if( (countAnimation %44 < 22) || (objectAttribute[mdata][ATR_CROP2] == 0) ) cropID = objectAttribute[mdata][ATR_CROP1];
                    else cropID = objectAttribute[mdata][ATR_CROP2];

                    if( CheckNoDrawParts(mdata,x,charaX /5,y,charaY /5) == false ){
                        gBuffCharaY.drawImage( imgCrop[cropID], 0, 0, this );
                    }
                }
            }
            ++y;
            if( (y +mapY *10) < g_iMapWidth ){
                mdata = mapObject[y +mapY *10][x +mapX *10];
                if( mdata != 0 ){
                    if( (countAnimation %44 < 22) || (objectAttribute[mdata][ATR_CROP2] == 0) ) cropID = objectAttribute[mdata][ATR_CROP1];
                    else cropID = objectAttribute[mdata][ATR_CROP2];

                    if( CheckNoDrawParts(mdata,x,charaX /5,y,charaY /5) == false ){
                        gBuffCharaY.drawImage( imgCrop[cropID], 0, 40, this );
                    }
                }
            }
            //フレーム描画
            if( x2 >= 0 ) paintFrame2( gBuffCharaY, x2, y2, 0, 0 );
            paintFrame2( gBuffCharaY, x2, y2+1, 0, 40 );

            x = (charaX /5) *40;
            y = (charaY /5) *40;
            if( (charaY %5 == 0) && (moveDirect == 2) ) y -= 40;
            g.drawImage( imgBuffCharaY, x, y, this );
        }

        //連続メッセージ表示中
        if( g_iPageNumber > 0 ) return;

        //マップキャラクタ描画
        for( j = 0 ; j < 11 ; ++j ){
            for( i = 0 ; i < 11 ; ++i ){
                if( (mapFlag[i][j] == true) && !((charaX /5 == i) && (charaY /5 == j)) ){
                    //プレーヤーキャラ８ドット切れ対策
                    if( ((charaX /5 +1 == i) && (charaY /5 == j) && (moveDirect == 6)) || ((charaY /5 +1 == j) && (charaX /5 == i) && (moveDirect == 2)) ){
                        if( (charaX %5 != 0) || (charaY %5 != 0) ) continue;
                    }
                    gBuffMap.setColor( Color.gray );
                    gBuffMap.fillRect( 0, 0, 40, 40 );

                    //背景描画
                    mdata = map[j +mapY *10 ][i +mapX *10];
                    cropID = mapAttribute[mdata][ATR_CROP1];
                    gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );

                    //オブジェクト描画
                    mdata = mapObject[j +mapY *10][i +mapX *10];
                    if( (mdata != 0) && (CheckNoDrawParts(mdata,charaX /5,i,charaY /5,j) == false) ){
                        if( (countAnimation %44 < 22) || (objectAttribute[mdata][ATR_CROP2] == 0) ){
                            cropID = objectAttribute[mdata][ATR_CROP1];
                        } else {
                            cropID = objectAttribute[mdata][ATR_CROP2];
                        }
                        gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
                    }
                    //フレーム描画
                    paintFrame2( gBuffMap, i, j, 0, 0 );

                    g.drawImage( imgBuffMap, i*40, j*40, this );
                }
            }
        }
    }



//////////////////////////////////////////////////
//マップ書き換え用のフラグを立てる

    public void setMapFlag()
    {
        //変数定義
        int i, j;

        //フラグを立てる
        for( j = 0 ; j < 11 ; ++j ){
            for( i = 0 ; i < 11 ; ++i ){
                mapFlag[i][j] = false;
                mdata = mapObject[j +mapY *10][i +mapX *10];
                if( (mdata != 0) && (countAnimation %11 == i) && (objectAttribute[mdata][ATR_CROP2] != 0) ){
                    mapFlag[i][j] = true;
                }
            }
        }
        ++countAnimation;
    }



//////////////////////////////////////////////////
//枠の描画

    public void paintFrame2( Graphics g, int x, int y, int x2, int y2 )
    {
        //フレーム描画
        cropID = 0;

        if( (x == 0) && (y == 0) ) cropID = CROP_MAINFRAME;
        else if( (x == 10) && (y == 0) ) cropID = CROP_MAINFRAME +2;
        else if( (x == 0) && (y == 10) ) cropID = CROP_MAINFRAME +20;
        else if( (x == 10) && (y == 10) ) cropID = CROP_MAINFRAME +22;
        else if( y == 0 ) cropID = CROP_MAINFRAME +1;
        else if( x == 0 ) cropID = CROP_MAINFRAME +10;
        else if( x == 10 ) cropID = CROP_MAINFRAME +12;
        else if( y == 10 ) cropID = CROP_MAINFRAME +21;

        if( cropID != 0 ) g.drawImage( imgCrop[cropID], x2, y2, this );
    }



//////////////////////////////////////////////////
//キーとマウスの共通処理

    public boolean KeyMouseDown( int key, int x, int y )
    {
        countRepaint = 0;

        //拡張クラス呼び出し
        if( (attackFlag == true) || (g_bStopInput == true) ) return true;

        if( displayMonsterFlag == true ){
            displayMonsterFlag = false;
            mapFlagAll = true;
            mapFlagErase = false;
        }
        //URLリンクからの復帰
        if( urlJumpFlag == true ){
            mapFlagAll = true;
            mapFlagErase = false;
            urlJumpFlag = false;
            System.gc();	//メモリ解放
            return true;
        }
        //外部ウィンドを閉じる
        if( g_bOpenFrameWin == true ){
            g_bOpenFrameWin = false;
            if( g_bOpenLoadPass == true ) ExtractLoadMapData();
            g_frameWin.dispose();
        }
        return false;
    }


    public void KeyMouseItem( int i )
    {
        if( objectAttribute[itemBox[i]][ATR_MODE] != 0 ){
            itemboxBuff = i;
            yesnoNumber = YESNO_USEITEM;
            yesnoFlag = true;
            g_iUseItem = itemBox[i];
        }
    }



//////////////////////////////////////////////////
//キーが押されたとき

    String g_szKeyList = "　【ショートカットキーの一覧】\nＦ１、Ｍ：戦闘結果予測の表示\nＦ２、Ｐ：移動速度の切り換え\nＦ３：復帰用パスワード入力\nＦ４：復帰用パスワード表示\nＦ５：一時保存データの読み込み\nＦ６：データの一時保存\nＦ７：初めからスタート\nＦ８：ＷＷＡ公式ページにリンク\nＦ９、Ｇ：描画モードの切り換え\nＦ１２：このリストの表示\nＬ：リンクを別のウィンドウで開く\nキーボードの「１２３、ＱＷＥ、\nＡＳＤ、ＺＸＣ」は\n右のアイテムボックスに対応。\n「Ｅｎｔｅｒ、Ｙ」はＹｅｓ\n「Ｅｓｃ、Ｎ」はＮｏに対応。\n　　現在の移動回数：";

    public boolean keyDown( Event e, int key )
    {
        //キーとマウスの共通処理
        if( KeyMouseDown( key, 0, 0 ) == true ) return true;

        //ＹＥＳ，ＮＯの判定
        if( yesnoNumber != YESNO_NONE ){
            if( (key == 10) || (key == 13) || (key == 'y') || (key == 'Y') ) yesnoJudge = YESNO_YES;
            if( (key == 27) || (key == 'n') || (key == 'N') ) yesnoJudge = YESNO_NO;
            return true;
        }
        //エンターキーまたはスペースキー，ＥＳＣキー
        if( ((key == 10) || (key == 13) || (key == 32) || (key == 27)) && (inputKey == false) ){
            //if( (soundFlag == true) && (inputKey == false) && (yesnoFlag == false) ) playAudio( 2 );
            inputKey = true;
            return true;
        }
        //連続メッセージ表示中はその他の処理をキャンセル
        if( g_iPageNumber > 0 ) return true;

        //モンスターの能力値表示
        if( ((key == Event.F1) || (key == 'm' || key == 'M')) && (inputKey == true) && (yesnoNumber == YESNO_NONE) ){
            displayMonsterFlag = true;
            inputKey = false;
        }

        //ホームページへ移動の場合
        if( key == Event.F8 ){
            yesnoNumber = YESNO_WWAH;
            yesnoFlag = true;
        }
        //リスタートの場合
        if( key == Event.F7 ){
            yesnoNumber = YESNO_RESTART;
            yesnoFlag = true;
        }
        //クイックセーブの場合
        if( key == Event.F6 ){
            yesnoNumber = YESNO_QSAVE;
            yesnoFlag = true;
        }
        if( key == Event.F5 ){
            if( quickSaveFlag == true ){
                yesnoNumber = YESNO_QLOAD;
            } else {
                yesnoNumber = YESNO_TEXTLOAD;
            }
            yesnoFlag = true;
        }
        //パスワードテキスト
        if( key == Event.F3 ){
            yesnoNumber = YESNO_TEXTLOAD;
            yesnoFlag = true;
        }
        if( key == Event.F4 ){
            yesnoNumber = YESNO_TEXTSAVE;
            yesnoFlag = true;
        }
        //対応表表示
        if( key == Event.F12 ){
            DisplayMessage( g_szKeyList +g_iStep, true );
        }

        //描画モード
        if( ((key == Event.F9) || (key == 'g' || key == 'G')) ){
            if( g_bPaintMapMove == true ){
                DisplayMessage( "高速描画（部分画面描画）モードに\n切り換えます。\n動作が重い低スペックのマシンで\n使用してください。", true );
                g_bPaintMapMove = false;
            } else {
                DisplayMessage( "通常描画（全画面描画）モードに\n切り換えます。", true );
                g_bPaintMapMove = true;
            }
            mapFlagAll = true;
        }
        //速度切り換え
        if( ((key == Event.F2) || (key == 'p' || key == 'P')) ){
            if( TimerCount == 20 ){
                DisplayMessage( "移動速度を高速に切り換えます。\n戦闘も速くなります。", true );
                TimerCount = 10;
            } else {
                DisplayMessage( "移動速度を通常に切り換えます。", true );
                TimerCount = 20;
            }
        }
        if( (key == 'i' || key == 'I') ){
            if( TimerCount != 60 ){
                DisplayMessage( "移動速度を低速に切り換えます。", true );
                TimerCount = 60;
            } else {
                DisplayMessage( "移動速度を通常に切り換えます。", true );
                TimerCount = 20;
            }
        }
        //リンクモード切り換え
        if( (key == 'l' || key == 'L') ){
            if( g_bPopup == false ){
                DisplayMessage( "リンクを別のウィンドウで\n開くようにします。", true );
                g_bPopup = true;
            } else if( g_bPopup == true ){
                DisplayMessage( "リンクを同じウィンドウで\n開くようにします。", true );
                g_bPopup = false;
            }
        }

        //if( (key == '8') || (key == Event.UP) ){
        if( key == Event.UP ){
            UpKey = true;
            currentKey = 8;
        }
        //else if( key == '2' || (key == Event.DOWN) ){
        else if( key == Event.DOWN ){
            DownKey = true;
            currentKey = 2;
        }
        //else if( key == '4' || (key == Event.LEFT) ){
        else if( key == Event.LEFT ){
            LeftKey = true;
            currentKey = 4;
        }
        //else if( key == '6' || (key == Event.RIGHT) ){
        else if( key == Event.RIGHT ){
            RightKey = true;
            currentKey = 6;
        }
        else if( key == Event.HOME ){
            mapFlagAll = true;
            mapFlagErase = true;
        }

        //アイテムボックスの場合
        int i;
        char cItemKey[] = { '1','!', '2','"', '3','#', 'q','Q', 'w','W', 'e','E', 'a','A', 's','S', 'd','D', 'z','Z', 'x','X', 'c','C' };
        for( i = 0 ; i < 12 ; ++i ){
            if( (key == cItemKey[i*2]) || (key == cItemKey[i*2+1]) ) KeyMouseItem( i );
        }
        //効果音
        if( (yesnoFlag == true) || (displayMonsterFlag == true) ) playAudio( 1 );

        return true;
    }



//////////////////////////////////////////////////
//キーが離されたとき

    public boolean keyUp( Event e, int key )
    {
        //カーソルキーの場合
        if( key == Event.UP ) UpKey = false;
        else if( key == Event.DOWN ) DownKey = false;
        else if( key == Event.LEFT ) LeftKey = false;
        else if( key == Event.RIGHT ) RightKey = false;
            //テンキーの場合
        else {
            UpKey = false;
            DownKey = false;
            LeftKey = false;
            RightKey = false;
        }
        return true;
    }



//////////////////////////////////////////////////
//マウスが押されたとき

    public boolean mouseDown( Event evt, int x, int y )
    {
        boolean bSound = false;

        //キーとマウスの共通処理
        if( KeyMouseDown( 0, x, y ) == true ) return true;

        if( inputKey == false ){
            inputKey = true;
            return true;
        }

        //ＹＥＳ，ＮＯの判定
        if( yesnoNumber != YESNO_NONE ){
            if( (x > yesnoX) && (x < yesnoX +40) && (y > yesnoY) && (y < yesnoY +40) ) yesnoJudge = YESNO_YES;
            if( (x > yesnoX+40) && (x < yesnoX+80) && (y > yesnoY) && (y < yesnoY+40) ) yesnoJudge = YESNO_NO;
            return true;
        }
        //連続メッセージ表示中はその他の処理をキャンセル
        if( g_iPageNumber > 0 ) return true;

        //対応表表示
        if( (x > 40*11) && (x < 40*14) && (y > 0) && (y < 35*4) && (yesnoNumber == YESNO_NONE) ){
            DisplayMessage( g_szKeyList +g_iStep, true );
        }

        //移動の場合
        int stanceX;
        int stanceY;
        if( (x > 0) && (x < 40*11) && (y > 0) && (y < 40*11) ){
            if( y >= charaY *8 +20 ){
                stanceY = y -(charaY *8 +20);
            } else {
                stanceY = (charaY *8 +20) -y;
            }
            if( x >= charaX *8 +20 ){
                stanceX = x -(charaX *8 +20);
            } else {
                stanceX = (charaX *8 +20) -x;
            }
            //下方向へ移動
            if( (charaY *8 +20 < y) && (stanceY > stanceX) ){
                DownKey = true;
            }
            //上方向へ移動
            else if( (charaY *8 +20 > y) && (stanceY > stanceX) ){
                UpKey = true;
            }
            //右方向へ移動
            else if( (charaX *8 +20 < x) && (stanceY < stanceX) ){
                RightKey = true;
            }
            //左方向へ移動
            else if( (charaX *8 + 20 > x) && (stanceY < stanceX) ){
                LeftKey = true;
            }
        }

        //ホームページへ移動の場合
        if( (x > 40*11) && (x < 40*14) && (y > 405) && (y < 440) ){
            yesnoNumber = YESNO_WWAH;
            yesnoFlag = true;
        }
        //リスタートの場合
        if( (x > 40*11) && (x < 40*14) && (y > 370) && (y < 405) ){
            yesnoNumber = YESNO_RESTART;
            yesnoFlag = true;
        }
        //クイックセーブの場合
        if( (x > 40*11) && (x < 40*14) && (y > 335) && (y < 370) ){
            yesnoNumber = YESNO_QSAVE;
            yesnoFlag = true;
        }
        if( (x > 40*11) && (x < 40*14) && (y > 300) && (y < 335) ){
            if( quickSaveFlag == true ){
                yesnoNumber = YESNO_QLOAD;
            } else {
                yesnoNumber = YESNO_TEXTLOAD;
            }
            yesnoFlag = true;
        }
        //アイテムボックスの場合
        int i;
        for( i = 0 ; i < 12 ; ++i ){
            if( (x > 40*(11+i%3)) && (x < 40*(12+i%3)) && (y > 140+40*(i/3)) && (y < 180+40*(i/3)) ){
                KeyMouseItem( i );
            }
        }

        //効果音
        if( (yesnoFlag == true) || (displayMonsterFlag == true) ) playAudio( 1 );

        return true;
    }



//////////////////////////////////////////////////
//マウスが離されたとき

    public boolean mouseUp( Event evt, int x, int y )
    {
        UpKey = false;
        DownKey = false;
        LeftKey = false;
        RightKey = false;

        return true;
    }



//////////////////////////////////////////////////
//移動方向の物体チェック

    public boolean CheckNoDrawParts( int iMapData, int x, int x2, int y, int y2 )
    {
        if( (charaX %5 != 0) || (charaY %5 != 0) ) return false;

        if( (x == x2) && (y == y2) && (g_bDefault == 0) ){
            if( ((objectAttribute[iMapData][ATR_NUMBER] == 0) && (objectAttribute[iMapData][ATR_TYPE] == OBJECT_DOOR))
                    || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_STATUS) || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_MESSAGE)
                    || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_ITEM) || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_SELL)
                    || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_BUY) || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_SELECT) || (objectAttribute[iMapData][ATR_TYPE] == OBJECT_LOCALGATE) ){
                return true;
            }
        }
        return false;
    }


    public void AnmItemSub( int i, int iBoxPos, int Xpoint, int Ypoint )
    {
        //アイテムアニメ用
        if( (g_bAnmItem == 1) && (g_bPaintMapMove == true) && (g_iAnmItemCount == 0) ){
            g_iAnmItemBox = i;
            if( iBoxPos != 0 ) g_iAnmItemBox = iBoxPos -1;
            g_iAnmItemOld = itemBox[g_iAnmItemBox];
            g_iAnmItemCount = 1;
            g_iAnmItemX = (Xpoint -mapX *10) *40;
            g_iAnmItemY = (Ypoint -mapY *10) *40;
            g_iAnmItemCountAdd = 10 -(Xpoint -mapX *10);
        }
    }


    public boolean directCheck( Graphics g, int Xpoint, int Ypoint )
    {
        //変数
        int i, j;
        int x, y;
        int number;
        boolean flag = true;
        int iMapNumber;
        int jumpX, jumpY;
        int iBoxPos;

        g_iXpoint = Xpoint;
        g_iYpoint = Ypoint;

        //マップチェック
        mdata = map[Ypoint][Xpoint];
        if( (mdata == 0) && (g_bOldMap == 0) ) flag = false;

        if( mapAttribute[mdata][ATR_TYPE] == MAP_WALL ){
            number = mapAttribute[mdata][ATR_STRING];
            if( number != 0 ){
                strNumber = number;
                messageFlag = true;
            }
            //キャラクタ出現
            appearChara( g, Xpoint, Ypoint, 1 );
            //サウンド
            if( mapAttribute[mdata][ATR_SOUND] != 0 ) playAudio( mapAttribute[mdata][ATR_SOUND] );
            //進行不可
            flag = false;
        }

        //オブジェクトチェック
        mdata = mapObject[Ypoint][Xpoint];
        iMapNumber = mdata;
        if( mdata != 0 ){
            flag = false;
            //飾り
            if( objectAttribute[mdata][ATR_TYPE] == OBJECT_NORMAL ){
                if( (objectAttribute[mdata][ATR_MODE] == 1) && (mapAttribute[map[Ypoint][Xpoint]][ATR_TYPE] != MAP_WALL) ) flag = true;
            }
            //ＵＲＬゲート
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_URLGATE ){
                messageFlag = false;
                strNumber = objectAttribute[mdata][ATR_STRING];
                jumpURL( new String(strMessage[strNumber]), true );
            }
            //ステータスアイテム
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_STATUS ){
                //マイナス判定
                if( ((objectAttribute[mdata][ATR_STRENGTH] > 30000) && (statusStrength < (objectAttribute[mdata][ATR_STRENGTH] -30000)))
                        || ((objectAttribute[mdata][ATR_DEFENCE] > 30000) && (statusDefence < (objectAttribute[mdata][ATR_DEFENCE] -30000)))
                        || ((objectAttribute[mdata][ATR_GOLD] > 30000) && (statusGold < (objectAttribute[mdata][ATR_GOLD] -30000))) ){
                    mapObject[Ypoint][Xpoint] = 0;
                    displayCharacter( g, Xpoint, Ypoint );
                    return false;
                }
                //生命力
                if( objectAttribute[mdata][ATR_ENERGY] > 30000 ){
                    statusEnergy -= (objectAttribute[mdata][ATR_ENERGY] -30000);
                    if( (statusEnergy <= 0) && (objectAttribute[mdata][ATR_ENERGY] != 0) ){
                        GameOver( g );
                        return false;
                    }
                } else {
                    statusEnergy += objectAttribute[mdata][ATR_ENERGY];
                }
                //攻撃力
                if( objectAttribute[mdata][ATR_STRENGTH] > 30000 ){
                    statusStrength -= (objectAttribute[mdata][ATR_STRENGTH] -30000);
                } else {
                    statusStrength += objectAttribute[mdata][ATR_STRENGTH];
                }
                //防御力
                if( objectAttribute[mdata][ATR_DEFENCE] > 30000 ){
                    statusDefence -= (objectAttribute[mdata][ATR_DEFENCE] -30000);
                } else {
                    statusDefence += objectAttribute[mdata][ATR_DEFENCE];
                }
                //所持金
                if( objectAttribute[mdata][ATR_GOLD] > 30000 ){
                    statusGold -= (objectAttribute[mdata][ATR_GOLD] -30000);
                } else {
                    statusGold += objectAttribute[mdata][ATR_GOLD];
                }
                if( objectAttribute[mdata][ATR_ENERGY] != 0 ) g_iChangeStatus[0] = 20;
                if( objectAttribute[mdata][ATR_STRENGTH] != 0 ) g_iChangeStatus[1] = 20;
                if( objectAttribute[mdata][ATR_DEFENCE] != 0 ) g_iChangeStatus[2] = 20;
                if( objectAttribute[mdata][ATR_GOLD] != 0 ) g_iChangeStatus[3] = 20;
                //ステータス表示
                displayConfigWindow( g, true, true, true, false );
                //メッセージ表示
                messageFlag = true;
                strNumber = objectAttribute[mdata][ATR_STRING];
                //物体消去
                mapObject[Ypoint][Xpoint] = 0;
                //キャラクタ出現
                appearChara( g, Xpoint, Ypoint, 0, iMapNumber );
            }
            //アイテム
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_ITEM ){
                for( i = 0 ; i < 12 ; ++i ){
                    if( itemBox[i] == 0 ) break;
                }
                //アイテムが１２個うまっている場合
                iBoxPos = objectAttribute[mdata][ATR_NUMBER];
                if( (i == 12) && ((iBoxPos == 0) || (objectAttribute[itemBox[iBoxPos-1]][ATR_NUMBER] == 0)) ){
                    messageFlag = true;
                    strNumber = 1;
                    if( g_szMessageSystem[1].equals("") ) strMessage[strNumber] = "これ以上、アイテムを持てません。";
                    else strMessage[strNumber] = g_szMessageSystem[1];
                } else {
                    if( objectAttribute[mdata][ATR_STRENGTH] != 0 ) g_iChangeStatus[1] = 20;
                    if( objectAttribute[mdata][ATR_DEFENCE] != 0 ) g_iChangeStatus[2] = 20;
                    //アイテムアニメ用
                    AnmItemSub( i, iBoxPos, Xpoint, Ypoint );
                    //アイテム整理
                    arrangeItem( mdata );
                    displayConfigWindow( g, true, true, true, false );
                    //物体消去
                    mapObject[Ypoint][Xpoint] = 0;
                    //使用型アイテムの場合
                    if( objectAttribute[mdata][ATR_MODE] != 0 ){
                        if( bDispUseItemMes == true ){
                            if( g_szMessageSystem[0].equals("") ) DisplayMessage( "このアイテムは右のボックスを\nクリックすることで使用できます。\n使用できるアイテムは\n色枠で囲まれます。", false );
                            else if( !(g_szMessageSystem[0].equals("BLANK")) ) DisplayMessage( g_szMessageSystem[0], false );
                            bDispUseItemMes = false;
                        }
                    } else {
                        //メッセージ表示
                        messageFlag = true;
                        strNumber = objectAttribute[mdata][ATR_STRING];
                        //キャラクタ出現
                        appearChara( g, Xpoint, Ypoint, 0, iMapNumber );
                    }
                }
            }
            //扉
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_DOOR ){
                for( i = 0 ; i < 12 ; ++i ){
                    if( objectAttribute[mdata][ATR_ITEM] == itemBox[i] ){
                        if( g_bNoExec == true ) return false;
                        //アイテム消去
                        if( objectAttribute[mdata][ATR_MODE] == 0 ){
                            itemBox[i] = 0;
                            arrangeItem( 0 );
                        }
                        displayConfigWindow( g, true, true, true, false );
                        //メッセージ表示
                        messageFlag = true;
                        strNumber = objectAttribute[mdata][ATR_STRING];
                        //物体消去
                        mapObject[Ypoint][Xpoint] = 0;
                        if( g_bPaintMapMove == true ) g_bSkipPaint = true;
                        //キャラクタ出現
                        appearChara( g, Xpoint, Ypoint, 0, iMapNumber );
                        movingSkip = 1;
                        if( objectAttribute[mdata][ATR_SOUND] != 0 ) playAudio( objectAttribute[mdata][ATR_SOUND] );
                        break;
                    }
                }
                if( (i == 12) && (objectAttribute[mdata][ATR_NUMBER] == 1) ) return true;
            }
            //モンスター
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_MONSTER ){
                attackFlag = true;
                attackFlagTurn = true;
                monsterEnergy = objectAttribute[mdata][ATR_ENERGY];
                monsterStrength = objectAttribute[mdata][ATR_STRENGTH];
                monsterDefence = objectAttribute[mdata][ATR_DEFENCE];
                monsterGold = objectAttribute[mdata][ATR_GOLD];
                attackXp = Xpoint;
                attackYp = Ypoint;
                messageFlag = false;
                g_iAttackTurn = 0;

                displayMonsterStatus( g, mdata );
                twait( 200 );
            }
            //メッセージキャラクタ
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_MESSAGE ){
                //g_iOldPartsObject = mdata;
                //待ち時間
                if( objectAttribute[mdata][ATR_NUMBER] != 0 ) twait( objectAttribute[mdata][ATR_NUMBER] *100, g );
                strNumber = objectAttribute[mdata][ATR_STRING];
                if( strNumber != 0 ) messageFlag = true;
                //プレーヤーと同じ位置ならば消去
                if( (Xpoint == playerX) && (Ypoint == playerY) && (g_bDefault == 0) ) mapObject[Ypoint][Xpoint] = 0;
                //キャラクタ出現
                appearChara( g, Xpoint, Ypoint, 0, iMapNumber );
            }
            //スコアキャラクタ
            if( objectAttribute[mdata][ATR_TYPE] == OBJECT_SCORE ){
                scoreFlag = true;
                messageFlag = true;
                strNumber = objectAttribute[mdata][ATR_STRING];
                //文字列がない場合
                if( strNumber == 0 ){
                    strNumber = 1;
                    strMessage[strNumber] = "スコアを表示します。";
                }
                score = 0;
                score += objectAttribute[mdata][ATR_ENERGY] * statusEnergy;
                score += objectAttribute[mdata][ATR_STRENGTH] *(statusStrength +itemStrength);
                score += objectAttribute[mdata][ATR_DEFENCE] *(statusDefence +itemDefence);
                score += objectAttribute[mdata][ATR_GOLD] *statusGold;
            }
            //物を買うキャラクタ
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_BUY ){
                mdataBuff = mdata;
                XpBuff = Xpoint;
                YpBuff = Ypoint;
                strNumber = objectAttribute[mdata][ATR_STRING];

                messageFlag = false;
                yesnoNumber = YESNO_BUY;
                yesnoFlag = true;

                //プレーヤーと同じ位置ならば消去
                if( (Xpoint == playerX) && (Ypoint == playerY) && (g_bDefault == 0) ) mapObject[Ypoint][Xpoint] = 0;
            }
            //物を売るキャラクタ
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_SELL ){
                mdataBuff = mdata;
                XpBuff = Xpoint;
                YpBuff = Ypoint;
                strNumber = objectAttribute[mdata][ATR_STRING];

                messageFlag = false;
                yesnoNumber = YESNO_SELL;
                yesnoFlag = true;

                //プレーヤーと同じ位置ならば消去
                if( (Xpoint == playerX) && (Ypoint == playerY) && (g_bDefault == 0) ) mapObject[Ypoint][Xpoint] = 0;
            }
            //二者択一
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_SELECT ){
                mdataBuff = mdata;
                XpBuff = Xpoint;
                YpBuff = Ypoint;
                strNumber = objectAttribute[mdata][ATR_STRING];

                messageFlag = false;
                yesnoNumber = YESNO_SELECT;
                yesnoFlag = true;

                //プレーヤーと同じ位置ならば消去
                if( (Xpoint == playerX) && (Ypoint == playerY) && (g_bDefault == 0) ) mapObject[Ypoint][Xpoint] = 0;
            }
            //ローカルゲートの場合
            else if( objectAttribute[mdata][ATR_TYPE] == OBJECT_LOCALGATE ){
                //プレーヤーと同じ位置ならば消去
                if( (Xpoint == playerX) && (Ypoint == playerY) && (g_bDefault == 0) ) mapObject[Ypoint][Xpoint] = 0;
                //キャラクタ出現
                appearChara( g, Xpoint, Ypoint, 0, iMapNumber );
                //位置設定
                jumpX = objectAttribute[mdata][ATR_JUMP_X];
                jumpY = objectAttribute[mdata][ATR_JUMP_Y];
                JumpPoint( jumpX, jumpY );
                //サウンド
                if( objectAttribute[mdata][ATR_SOUND] != 0 ) playAudio( objectAttribute[mdata][ATR_SOUND] );
                return false;
            }
            //サウンド
            if( (objectAttribute[mdata][ATR_TYPE] != OBJECT_MONSTER) && (objectAttribute[mdata][ATR_TYPE] != OBJECT_DOOR) ) playAudio( objectAttribute[mdata][ATR_SOUND] );
        }

        //進行方向再描画
        x =  Xpoint %10;
        if( (x == 0) && ((charaX /5) >= 9) ) x = 10;
        y =  Ypoint %10;
        if( (y == 0) && ((charaY /5) >= 9) ) y = 10;
        mapFlag[x][y] = true;

        return flag;
    }



//////////////////////////////////////////////////
//現在位置の背景パーツチェック

    int oldMapData;
    int oldObjectData;
    int oldMapX, oldMapY;

    public boolean pointCheckMap( Graphics g, int mapData,int x, int y )
    {
        int i;
        int number;

        g_iXpoint = playerX;
        g_iYpoint = playerY;

        //道の場合
        if( mapAttribute[mapData][ATR_TYPE] == MAP_STREET ){
            //特定のアイテムのみに反応する場合
            if( mapAttribute[mapData][ATR_ITEM] != 0 ){
                for( i = 0 ; i < 12 ; ++i ){
                    if( itemBox[i] == mapAttribute[mapData][ATR_ITEM] ) break;
                }
                if( i == 12 ) return false;
            }
            //キャラクタ出現
            appearChara( g, x+mapX*10, y+mapY*10, 1 );
            //サウンド
            if((mapAttribute[mapData][ATR_SOUND] != 0) && (soundFlag == true)) playAudio( mapAttribute[mapData][ATR_SOUND] );
            //待ち時間
            if( mapAttribute[mapData][ATR_NUMBER] != 0 ) twait( mapAttribute[mapData][ATR_NUMBER] *100, g );
            //メッセージ
            number = mapAttribute[mapData][ATR_STRING];
            if( number != 0 ){
                strNumber = number;
                messageFlag = true;
                return true;
            }
            if( mapAttribute[mapData][ATR_NUMBER] != 0 ) return true;
        }
        //ローカルゲートの場合
        else if( mapAttribute[mapData][ATR_TYPE] == MAP_LOCALGATE ){
            //キャラクタ出現
            appearChara( g, playerX, playerY, 1 );
            //位置設定
            int jumpX = mapAttribute[mapData][ATR_JUMP_X];
            int jumpY = mapAttribute[mapData][ATR_JUMP_Y];
            JumpPoint( jumpX, jumpY );
            //サウンド
            if((mapAttribute[mapData][ATR_SOUND] != 0) && (soundFlag == true)) playAudio( mapAttribute[mapData][ATR_SOUND] ); //audio[mapAttribute[mapData][ATR_SOUND]].play();
            return true;
        }
        //ＵＲＬゲート
        else if( mapAttribute[mapData][ATR_TYPE] == MAP_URLGATE ){
            strNumber = mapAttribute[mapData][ATR_STRING];
            jumpURL( new String(strMessage[strNumber]), true );
            return true;
        }
        return false;
    }


    public boolean pointCheck( Graphics g )
    {
        //int i;
        int x, y;
        //int number;
        int mapData;
        int objectData;

        //マップチェック
        x = charaX /5;
        y = charaY /5;
        mapData = map[y+mapY*10][x+mapX*10];
        objectData = mapObject[y+mapY*10][x+mapX*10];

        //位置が同じかチェック（背景）
        if( !((mapData == oldMapData) && (oldMapX == x+mapX*10) && (oldMapY == y+mapY*10)) ){
            oldMapX = x +mapX *10;
            oldMapY = y +mapY *10;
            oldMapData = mapData;
            //背景チェック
            if( pointCheckMap(g,mapData,x,y) == true ) return true;
        }
        //位置が同じかチェック（物体）
        if( !((objectData == oldObjectData) && (oldMapX == x+mapX*10) && (oldMapY == y+mapY*10)) ){
            oldMapX = x +mapX *10;
            oldMapY = y +mapY *10;
            oldObjectData = objectData;
            //現在位置の物体チェック
            objectData = mapObject[y+mapY*10][x+mapX*10];
            if( objectAttribute[objectData][ATR_TYPE] != 0 ){
                displayCharacter( g, x+mapX*10, y+mapY*10 );
                //物体チェック
                directCheck( g, charaX/5+mapX*10, charaY/5+mapY*10 );
                return true;
            }
        }
        return false;
    }



//////////////////////////////////////////////////
//ジャンプ設定

    public boolean JumpPointSub( int x, int y, int iDirect )
    {
        int iMapData;

        //四方にゲートがあれば向き変更
        if( (x >= 0) && (x <= g_iMapWidth-1) && (y >= 0) && (y <= g_iMapWidth-1) ){
            iMapData = map[y][x];
            if( mapAttribute[iMapData][ATR_TYPE] == MAP_LOCALGATE ){
                SetDirectPlayer( iDirect );
                return true;
            }
            iMapData = mapObject[y][x];
            if( objectAttribute[iMapData][ATR_TYPE] == OBJECT_LOCALGATE ){
                SetDirectPlayer( iDirect );
                return true;
            }
        }
        return false;
    }


    public void JumpPoint( int jumpX, int jumpY )
    {
        int iMapData;

        if( jumpX > 9000 ) jumpX = playerX + jumpX - 10000;
        if( jumpY > 9000 ) jumpY = playerY + jumpY - 10000;

        if( (jumpX >= 0) && (jumpX <= g_iMapWidth-1) && (jumpY >= 0) && (jumpY <= g_iMapWidth-1) ){
            int mapXb = mapX;
            int mapYb = mapY;
            if( jumpX == g_iMapWidth-1 ){
                mapX = jumpX /10 -1;
                charaX = 10 *5;
            } else {
                mapX = jumpX /10;
                charaX = (jumpX %10) *5;
            }
            if( jumpY == g_iMapWidth-1 ){
                mapY = jumpY /10 -1;
                charaY = 10 *5;
            } else {
                mapY = jumpY /10;
                charaY = (jumpY %10) *5;
            }
            mapFlagAll = true;

            //同一画面なら書き換えしない
            if( (mapX == mapXb) && (mapY == mapYb) ){
                mapFlagErase = false;
            } else {
                mapFlagErase = true;
                g_bRestPlayer = true;
            }
            //プレーヤーの現在位置取得
            playerX = charaX /5 +mapX *10;
            playerY = charaY /5 +mapY *10;

            //四方にゲートがあれば向き変更
            if( loadingFlag == true ){
                //現在位置がゲートなら正面
                SetDirectPlayer( 2 );
                if( JumpPointSub( playerX, playerY, 2 ) == false ){
                    JumpPointSub( (playerX -1), playerY, 6 );
                    JumpPointSub( (playerX +1), playerY, 4 );
                    JumpPointSub( playerX, (playerY -1), 2 );
                    JumpPointSub( playerX, (playerY +1), 8 );
                }
            }
        }
    }



//////////////////////////////////////////////////
//キャラクタの移動

    public void moveCharacter( Graphics g )
    {
        //変数
        int i;
        int x, y;
        char key;
        int iCount;

        //プレーヤーターンをスキップ
        if( g_iTurnSkip > 0 ){
            if( g_iTurnSkip %5 == 0 ) WonderingChara();
            --g_iTurnSkip;
            return;
        }

        if( (charaX %5 == 0) && (charaY %5 == 0) ){
            //現在位置の背景チェック
            if( pointCheck(g) == true ) return;

            //キー入力チェック
            if( (UpKey == true) && (currentKey == 8) ) key = 8;
            else if( (DownKey == true) && (currentKey == 2) ) key = 2;
            else if( (LeftKey == true) && (currentKey == 4) ) key = 4;
            else if( (RightKey == true) && (currentKey == 6) ) key = 6;
            else key = 0;

            //一定時間キー入力がないなら書き換える
            if( (UpKey == false) && (DownKey == false) && (LeftKey == false) && (RightKey == false) ){
                if( countRepaint >= 10 ){
                    if( g_bPaintMapMove == false ){
                        x = mapX *10 +(countRepaint %11);
                        for( i = 0 ; i < 11 ; ++i ){
                            y = mapY *10 +i;
                            if( objectAttribute[mapObject[y][x]][ATR_CROP2] == 0 ) displayCharacter( g, x, y );
                        }
                    }
                    if( (countRepaint %11) == 0 ) displayConfigWindow( g, false, false, false, false );
                }
                ++countRepaint;
            } else {
                countRepaint = 0;
            }
            //開始またはジャンプ後に一定時間入力がなければ足踏み開始
            if( g_bRestPlayer == true ){
                if( TimerCount <= 10 ) iCount = countRepaint /2;
                else iCount = countRepaint;
                if( (iCount >= 64) && (iCount %8 == 0) && (countRepaint %4 == 0) ){
                    if( iCount %16 == 0 ){
                        if( moveDirect == 2 ) moveDirect = 4;
                        else if( moveDirect == 4 ) moveDirect = 8;
                        else if( moveDirect == 6 ) moveDirect = 2;
                        else if( moveDirect == 8 ) moveDirect = 6;
                        SetDirectPlayer( moveDirect );
                    } else {
                        SetDirectPlayer( moveDirect );
                        ++cropIDchara;
                    }
                }
            }
            //上のキーが押された時
            if( (key == 8) || ((key == 0) && (UpKey == true)) ){
                moveDirect = 8;
                //画面切り換え
                if( (charaY == 0) && (mapY != 0) ){
                    if( (map[playerY-1][playerX] != 0) || (g_bOldMap == 1) ){
                        --mapY;
                        mapFlagAll = true;
                        mapFlagErase = true;
                        g_bMapFlagMove = true;
                        charaY = 5*10;
                    }
                }
                else if( charaY != 0 ){
                    //物体チェック
                    x = charaX /5 +mapX *10;
                    y = charaY /5 +mapY *10 -1;
                    if( directCheck(g,x,y) == true ){
                        charaY = charaY - movePlus;
                        playerY = y;
                        WonderingChara();
                    }
                }
            }
            //下のキーが押された時
            else if( (key == 2) || ((key == 0) && (DownKey == true)) ){
                moveDirect = 2;
                //画面切り換え
                if( (charaY == 5*10) && (mapY != g_iMapWidth/10-1) ){
                    if( (map[playerY+1][playerX] != 0) || (g_bOldMap == 1) ){
                        ++mapY;
                        mapFlagAll = true;
                        mapFlagErase = true;
                        g_bMapFlagMove = true;
                        charaY = 0;
                    }
                }
                else if( charaY != 5*10 ){
                    //物体チェック
                    x = charaX /5 +mapX *10;
                    y = charaY /5 +mapY *10 +1;
                    if( directCheck(g,x,y) == true ){
                        charaY = charaY + movePlus;
                        playerY = y;
                        WonderingChara();
                    }
                }
            }
            //左のキーが押された時
            else if( (key == 4) || ((key == 0) && (LeftKey == true)) ){
                moveDirect = 4;
                //画面切り換え
                if( (charaX == 0) && (mapX != 0) ){
                    if( (map[playerY][playerX-1] != 0) || (g_bOldMap == 1) ){
                        --mapX;
                        mapFlagAll = true;
                        mapFlagErase = true;
                        g_bMapFlagMove = true;
                        charaX = 5*10;
                    }
                }
                else if( charaX != 0 ){
                    //物体チェック
                    x = charaX /5 +mapX *10 -1;
                    y = charaY /5 +mapY *10;
                    if( directCheck(g,x,y) == true ){
                        charaX = charaX - movePlus;
                        playerX = x;
                        WonderingChara();
                    }
                }
            }
            //右のキーが押された時
            else if( (key == 6) || ((key == 0) && (RightKey == true)) ){
                moveDirect = 6;
                //画面切り換え
                if( (charaX == 5*10) && (mapX != g_iMapWidth/10-1) ){
                    if( (map[playerY][playerX+1] != 0) || (g_bOldMap == 1) ){
                        ++mapX;
                        mapFlagAll = true;
                        mapFlagErase = true;
                        g_bMapFlagMove = true;
                        charaX = 0;
                    }
                }
                else if( charaX != 5*10 ){
                    //物体チェック
                    x = charaX /5 +mapX *10 +1;
                    y = charaY /5 +mapY *10;
                    if( directCheck(g,x,y) == true ){
                        charaX = charaX + movePlus;
                        playerX = x;
                        WonderingChara();
                    }
                }
            }
        } else {
            if( moveDirect == 8 ) charaY = charaY - movePlus;
            else if( moveDirect == 2 ) charaY = charaY + movePlus;
            else if( moveDirect == 4 ) charaX = charaX - movePlus;
            else if( moveDirect == 6 ) charaX = charaX + movePlus;
            //プレーヤーの現在位置取得
            playerX = charaX /5 +mapX *10;
            playerY = charaY /5 +mapY *10;
        }
    }



//////////////////////////////////////////////////
//日本語の表示

    public String getJapaneseToken( String str, int i )
    {
        int j;
        String szToken;
        int iIndex;

        //行文字列を取得
        g_iLast = i -1;
        szToken = tokenCutStirng( str, "\n" );
        //小文字に変換
        szToken = szToken.toLowerCase();
        szToken = szToken.trim();
        szToken = szToken.replace( '.', ',' );
        //空白削除
        for( j = 0 ; j < szToken.length() ; ++j ){
            iIndex = szToken.indexOf( " ", 0 );
            if( iIndex != -1 ) szToken = szToken.substring( 0, iIndex ) +szToken.substring( iIndex +1 );
            else break;
        }

        return szToken;
    }


    public String tokenCutStirng( String szToken, String szCut )
    {
        int iIndex;
        String szWork = "";

        if( g_iLast == -2 ) return szWork;
        iIndex = g_iLast +1;
        g_iLast = szToken.indexOf( szCut, iIndex );
        if( g_iLast == -1 ){
            g_iLast = -2;
            szWork = szToken.substring( iIndex );
        } else {
            szWork = szToken.substring( iIndex, g_iLast );
        }

        return szWork;
    }


    public int indexOfSub( int iNumber, String szCompare, String szToken, int strWidth )
    {
        int iIndex;

        iIndex = szToken.indexOf( szCompare );
        if( (iIndex != -1) && (iIndex < strWidth) ) return iNumber;
        return 0;
    }


    public int drawJapanese( Graphics g, String str, int xp, int yp, int strWidth, boolean bNoDraw, boolean bNoPage, boolean bMacro )
    {
        //変数定義
        int i, j;
        int x, y;
        int xp2, yp2;
        int iStrLine = 1;
        int iMode = 0;
        int iIndex;
        int strWidthCurrent;	//文字列の幅
        int iLast;
        int iLength;
        int iBox, iItem;
        int iParts;
        int iLimited;
        short iSourceParts;
        short iReplaceParts;
        char buff[] = new char[1];
        String szWork;
        String szWorkBuff;
        String szToken = new String("");
        int iData, iAtr;
        int iXpoint, iYpoint;
        String szStrX, szStrY;
        int iCrop, iXtop, iYtop, iXstart, iYstart, iXsize, iYsize;

        //フォント設定
        if( bNoDraw == false ){
            Font fon = new Font( "Monospaced", Font.PLAIN, 18 );
            g.setFont(fon);
        }

        //文末の改行削除
        iIndex = str.indexOf( "\n", (str.length() -1) );
        if( iIndex == (str.length() -1) ){
            str = str.substring( 0, (str.length() -1) );
        }

        //初期化
        strWidthCurrent = 0;
        xp2 = xp;
        yp2 = yp;
        szWork = str;
        szWorkBuff = szWork.toLowerCase();
        //改ページ検出
        iIndex = szWorkBuff.indexOf( "<p>" );
        if( (iIndex != -1) && (g_iPageNumber >= 0) ){
            //頭だし
            for( i = 0 ; i < g_iPageNumber ; ++i ){
                iIndex = szWorkBuff.indexOf( "<p>" );
                iIndex += 3;
                if( iIndex >= szWork.length() ) --iIndex;
                if( szWork.charAt(iIndex) == 10 ) ++iIndex;
                szWork = szWork.substring( iIndex, szWork.length() );
                szWorkBuff = szWork.toLowerCase();
            }
            if( bNoPage == false ) ++g_iPageNumber;

            //表示文字列作成
            iIndex = szWorkBuff.indexOf( "<p>" );
            if( iIndex != -1 ){
                str = szWork.substring( 0, iIndex );
                messageFlag = true;
            } else {
                str = szWork;
                if( bNoPage == false ) g_iPageNumber = -1;
            }
        }

        iLength = str.length();
        if( iLength == 0 ) return 0;
        for( i = 0 ; i < iLength ; ++i ){
            if( strWidthCurrent == 0 ){
                //行文字列を取得
                szToken = getJapaneseToken( str, i );
                //プレーヤー画像の変更
                iMode = indexOfSub( 1, "$imgplayer=", szToken, strWidth );
                //YESNO画像変更
                if( iMode == 0 ) iMode = indexOfSub( 2, "$imgyesno=", szToken, strWidth );
                //生命力最大値変更
                if( iMode == 0 ) iMode = indexOfSub( 3, "$hpmax=", szToken, strWidth );
                //セーブ許可
                if( iMode == 0 ) iMode = indexOfSub( 4, "$save=", szToken, strWidth );
                //アイテム
                if( iMode == 0 ) iMode = indexOfSub( 5, "$item=", szToken, strWidth );
                //デフォルト設定
                if( iMode == 0 ) iMode = indexOfSub( 6, "$default=", szToken, strWidth );
                //旧マップ互換
                if( iMode == 0 ) iMode = indexOfSub( 7, "$oldmap=", szToken, strWidth );
                //パーツ入れ換え
                if( iMode == 0 ) iMode = indexOfSub( 8, "$parts=", szToken, strWidth );
                //ターンスキップ
                if( iMode == 0 ) iMode = indexOfSub( 9, "$move=", szToken, strWidth );
                //パーツ入れ換え
                if( iMode == 0 ) iMode = indexOfSub( 10, "$map=", szToken, strWidth );
                //進行方向パーツ入れ換え
                if( iMode == 0 ) iMode = indexOfSub( 11, "$dirmap=", szToken, strWidth );
                //枠画像変更
                if( iMode == 0 ) iMode = indexOfSub( 12, "$imgframe=", szToken, strWidth );
                if( iMode == 0 ) iMode = indexOfSub( 13, "$imgbom=", szToken, strWidth );
                //プレーヤー画像消去
                if( iMode == 0 ) iMode = indexOfSub( 14, "$delplayer=", szToken, strWidth );
                //フェイス表示
                if( iMode == 0 ) iMode = indexOfSub( 15, "$face=", szToken, strWidth );
                //特殊画面効果
                if( iMode == 0 ) iMode = indexOfSub( 16, "$effect=", szToken, strWidth );
                //ゲームオーバー座標
                if( iMode == 0 ) iMode = indexOfSub( 17, "$gameover=", szToken, strWidth );
                //使用型アイテム
                if( iMode == 0 ) iMode = indexOfSub( 18, "$imgclick=", szToken, strWidth );
                //ステータス変更
                if( iMode == 0 ) iMode = indexOfSub( 19, "$status=", szToken, strWidth );
                //ステータス変更
                if( iMode == 0 ) iMode = indexOfSub( 20, "$effitem=", szToken, strWidth );
                //行頭に＄か＜があればスキップ
                //if( (iMode == 0) && ((szToken.indexOf("$",0) == 0) || (szToken.indexOf("<",0) == 0)) ) iMode = 99;
                if( (iMode == 0) && (szToken.indexOf("$",0) == 0) ) iMode = 99;

                if( iMode != 0 ){
                    g_iLast = -1;
                    tokenCutStirng( szToken, "=" );
                    //マクロは重複処理しない（フェイス表示以外）
                    if( (bMacro == false) && (iMode != 15) ) iMode = 0;
                    //各種変更
                    if( (iMode == 1) || (iMode == 2) || (iMode == 13) || (iMode == 17) || (iMode == 18) ){
                        //画像位置変更
                        x = parse( tokenCutStirng( szToken, "," ) );
                        y = parse( tokenCutStirng( szToken, "," ) );
                        if( iMode == 1 ) g_iImgCharaCrop = x +y *10;
                        else if( iMode == 2 ) SetYesNoCrop( x +y *10 );
                        else if( iMode == 13 ) CROP_BOM = x +y *10;
                        else if( iMode == 17 ){
                            gameoverXp = x;
                            gameoverYp = y;
                        }
                        else if( iMode == 18 ) g_iImgClickItem = x +y *10;
                    }
                    else if( iMode == 3 ) statusEnergyMax = parse( tokenCutStirng( szToken, "," ) );
                    else if( iMode == 4 ) g_bSaveStop = parse( tokenCutStirng( szToken, "," ) );
                        //アイテム
                    else if( iMode == 5 ){
                        if( bNoDraw == true ){
                            iBox = parse( tokenCutStirng( szToken, "," ) );
                            iItem = parse( tokenCutStirng( szToken, "," ) );
                            if( iBox == 0 ){
                                arrangeItem( iItem );
                            } else {
                                itemBox[iBox-1] = iItem;
                                arrangeItem( 0 );
                            }
                        }
                        //アイテムボックス再描画
                        configFlag = true;
                    }
                    else if( iMode == 6 ) g_bDefault = parse( tokenCutStirng( szToken, "," ) );
                    else if( iMode == 7 ) g_bOldMap = parse( tokenCutStirng( szToken, "," ) );
                        //パーツ入れ換え
                    else if( iMode == 8 ){
                        iSourceParts = (short)parse( tokenCutStirng( szToken, "," ) );
                        iReplaceParts = (short)parse( tokenCutStirng( szToken, "," ) );
                        iParts = parse( tokenCutStirng( szToken, "," ) );
                        iLimited = parse( tokenCutStirng( szToken, "," ) );
                        if( iLimited == 0 ){
                            for( x = 0 ; x < g_iMapWidth ; ++x ){
                                for( y = 0 ; y < g_iMapWidth ; ++y ){
                                    if( (iParts == 1) && (iSourceParts == map[y][x]) ) map[y][x] = iReplaceParts;
                                    else if( (iParts == 0) && (iSourceParts == mapObject[y][x]) ) mapObject[y][x] = iReplaceParts;
                                }
                            }
                        } else {
                            for( x = 0 ; x < 11 ; ++x ){
                                for( y = 0 ; y < 11 ; ++y ){
                                    if( (iParts == 1) && (iSourceParts == map[y +mapY *10][x +mapX *10]) ) map[y +mapY *10][x +mapX *10] = iReplaceParts;
                                    else if( (iParts == 0) && (iSourceParts == mapObject[y +mapY *10][x +mapX *10]) ) mapObject[y +mapY *10][x +mapX *10] = iReplaceParts;
                                }
                            }
                        }
                        if( g_bPaintMapMove == false ) mapFlagAll = true;
                    }
                    //ターンスキップ
                    else if( iMode == 9 ){
                        g_iTurnSkip = parse( tokenCutStirng( szToken, "," ) ) *5;
                    }
                    //パーツ出現
                    else if( iMode == 10 ){
                        //System.out.println(szToken);
                        iData = parse( tokenCutStirng( szToken, "," ) );
                        szStrX = tokenCutStirng( szToken, "," );
                        x = parse( szStrX );
                        szStrY = tokenCutStirng( szToken, "," );
                        y = parse( szStrY );
                        iAtr = parse( tokenCutStirng( szToken, "," ) );
                        iXpoint = g_iXpoint;
                        iYpoint = g_iYpoint;
                        //相対座標も判定
                        if( szStrX.indexOf("+",0) == 0 ){
                            szStrX = szStrX.replace( '+' ,'0' );
                            x = parse( szStrX );
                            iXpoint += x;
                        } else if( szStrX.indexOf("-",0) == 0 ) iXpoint += x;
                        else iXpoint = x;

                        if( szStrY.indexOf("+",0) == 0 ){
                            szStrY = szStrY.replace( '+' ,'0' );
                            y = parse( szStrY );
                            iYpoint += y;
                        } else if( szStrY.indexOf("-",0) == 0 ) iYpoint += y;
                        else iYpoint = y;
                        //プレーヤー位置
                        if( szStrX.indexOf("p",0) == 0 ) iXpoint = playerX;
                        if( szStrY.indexOf("p",0) == 0 ) iYpoint = playerY;
                        //パーツ出現
                        if( (iXpoint >= 0) && (iXpoint <= g_iMapWidth-1) && (iYpoint >= 0) && (iYpoint <= g_iMapWidth-1) ){
                            if( iAtr == 0 ) mapObject[iYpoint][iXpoint] = (short)iData;
                            else map[iYpoint][iXpoint] = (short)iData;
                        }
                        if( g_bPaintMapMove == false ) mapFlagAll = true;
                    }
                    //パーツ出現
                    else if( iMode == 11 ){
                        iData = parse( tokenCutStirng( szToken, "," ) );
                        x = parse( tokenCutStirng( szToken, "," ) );
                        iAtr = parse( tokenCutStirng( szToken, "," ) );
                        iXpoint = playerX;
                        iYpoint = playerY;
                        if( moveDirect == 6 ) iXpoint += x;
                        else if( moveDirect == 4 ) iXpoint -= x;
                        else if( moveDirect == 2 ) iYpoint += x;
                        else if( moveDirect == 8 ) iYpoint -= x;
                        //パーツ出現
                        if( (iXpoint >= 0) && (iXpoint <= g_iMapWidth-1) && (iYpoint >= 0) && (iYpoint <= g_iMapWidth-1) ){
                            if( iAtr == 0 ) mapObject[iYpoint][iXpoint] = (short)iData;
                            else map[iYpoint][iXpoint] = (short)iData;
                        }
                        if( g_bPaintMapMove == false ) mapFlagAll = true;
                    }
                    //枠画像変更
                    else if( iMode == 12 ){
                        iAtr = parse( tokenCutStirng( szToken, "," ) );
                        x = parse( tokenCutStirng( szToken, "," ) );
                        y = parse( tokenCutStirng( szToken, "," ) );
                        if( iAtr == 0 ) CROP_ENERGY = x +y *10;
                        else if( iAtr == 1 ) CROP_STRENGTH = x +y *10;
                        else if( iAtr == 2 ) CROP_DEFENCE = x +y *10;
                        else if( iAtr == 3 ) CROP_GOLD = x +y *10;
                        else if( iAtr == 4 ) CROP_STFRAME = x +y *10;
                        else if( iAtr == 5 ) CROP_ITEMFRAME = x +y *10;
                        else if( iAtr == 6 ) CROP_MAINFRAME = x +y *10;
                    }
                    //プレーヤー画像消去
                    else if( iMode == 14 ){
                        g_bDelPlayer = parse( tokenCutStirng( szToken, "," ) );
                    }
                    //フェイス表示
                    else if( iMode == 15 ){
                        iXtop = parse( tokenCutStirng( szToken, "," ) );
                        iYtop = parse( tokenCutStirng( szToken, "," ) );
                        iXstart = parse( tokenCutStirng( szToken, "," ) );
                        iYstart = parse( tokenCutStirng( szToken, "," ) );
                        iXsize = parse( tokenCutStirng( szToken, "," ) );
                        iYsize = parse( tokenCutStirng( szToken, "," ) );
                        for( x = 0 ; x < iXsize ; ++x ){
                            for( y = 0 ; y < iYsize ; ++y ){
                                iCrop = (x +iXstart) +(y +iYstart) *10;
                                g.drawImage( imgCrop[iCrop], iXtop +x *40, iYtop +y *40, this );
                            }
                        }
                    }
                    //特殊画面効果
                    else if( iMode == 16 ){
                        g_iEffWait = parse( tokenCutStirng( szToken, "," ) );
                        for( j = 0 ; j < 4 ; ++j ){
                            iXtop = parse( tokenCutStirng( szToken,"," ) );
                            iYtop = parse( tokenCutStirng( szToken,"," ) );
                            g_iEffCrop[j] = iXtop +iYtop *10;
                        }
                    }
                    //ステータス変更
                    else if( iMode == 19 ){
                        iAtr = parse( tokenCutStirng( szToken, "," ) );
                        if( iAtr == 0 ) statusEnergy = parse( tokenCutStirng( szToken, "," ) );
                        else if( iAtr == 1 ) statusStrength = parse( tokenCutStirng( szToken, "," ) );
                        else if( iAtr == 2 ) statusDefence = parse( tokenCutStirng( szToken, "," ) );
                        else if( iAtr == 3 ) statusGold = parse( tokenCutStirng( szToken, "," ) );
                        else if( iAtr == 4 ) g_iStep = parse( tokenCutStirng( szToken, "," ) );
                    }
                    else if( iMode == 20 ) g_bAnmItem = parse( tokenCutStirng( szToken, "," ) );
                    //文字列整理
                    iIndex = str.indexOf( "\n", i );
                    if( (i == 0) && (iIndex == -1) ){
                        return 0;
                    } else {
                        i = iIndex;
                        if( i == -1 ) return (iStrLine -1);
                        continue;
                    }
                }
                //コメント以降を削除
                iIndex = szToken.indexOf( "<c>" );
                if( (iIndex != -1) && (iIndex < strWidth) ){
                    //文字列整理
                    if( i == 0 ){
                        return 0;
                    } else {
                        return (iStrLine -1);
                    }
                }
            }
            //改行の場合
            if( str.charAt(i) == 10 ){
                xp2 = xp;
                yp2 += 22;
                strWidthCurrent = 0;
                //終行の改行は含めない
                if( i < (iLength -1) ) ++iStrLine;
                continue;
            }
            buff[0] = str.charAt(i);

            if( ((buff[0] & 0xff00) == 0) && (buff[0] != '×') && (buff[0] != '÷') ){
                if( bNoDraw == false ){
                    g.setColor( Color.gray );
                    g.drawString( new String(buff), xp2 +1 +1, yp2 +17 );
                    g.setColor( Color.black );
                    g.drawString( new String(buff), xp2 +1, yp2 +17 );
                }
                //半角文字
                xp2 += 10;
                ++strWidthCurrent;
            } else {
                if( bNoDraw == false ){
                    g.setColor( Color.gray );
                    g.drawString( new String(buff), xp2 +2 +1, yp2 +16 );
                    g.setColor( Color.black );
                    g.drawString( new String(buff), xp2 +2, yp2 +16 );
                }
                //全角文字
                xp2 += 20;
                strWidthCurrent += 2;
            }
            if( strWidthCurrent >= strWidth *2 ){
                if( i < (iLength -1) ){
                    //行末が濁点、改行の場合はスキップ
                    if( (str.charAt(i+1) == '。') || (str.charAt(i+1) == '、') || (str.charAt(i+1) == 10) ) continue;
                }
                xp2 = xp;
                yp2 += 22;
                strWidthCurrent = 0;
                //終行の改行は含めない
                if( i < (iLength -1) ) ++iStrLine;
            }
        }
        return iStrLine;
    }



//////////////////////////////////////////////////
//吹出し日本語表示

    public void drawJapaneseFrameSub( Graphics g, String str, int xp, int yp, int xsize, int ysize, boolean bNoPage )
    {
        g.setColor( new Color(96,96,96) );
        g.fillRoundRect( xp-2, yp-2, xsize+4, ysize+4, 30, 30 );
        g.setColor( Color.white );
        g.fillRoundRect( xp, yp, xsize, ysize, 30, 30 );
        drawJapanese( g, str, xp+7, yp+9, 16, false, bNoPage, false );
        if( yesnoFlag == true ){
            g.drawImage( imgCrop[CROP_YES], yesnoX+2, g_iYesnoY+2, this );
            g.drawImage( imgCrop[CROP_NO], yesnoX+2 +40, g_iYesnoY+2, this );
        }
    }


    public boolean drawJapaneseFrame( Graphics g, Graphics gGlobal, String str, int xp, int yp )
    {
        int xsize, ysize;
        xsize = 20*16+20;
        ysize = 22*7+20;
        int iStrLine;

        //文字列の行数設定$$
        iStrLine = drawJapanese( g, str, 0, 0, 16, true, true, true );
        if( iStrLine == 0 ) return false;
        ysize = 22*iStrLine +20;
        if( yesnoFlag == true ) ysize += 40;

        //中央吹出し
        if( yp == -3 ){
            yp = 440 /2 -(ysize /2);
        }
        //上付き吹出し
        if( yp == -2 ){
            yp = 440 /4 -(ysize /2) +10;
            if( yp < 20 ) yp = 20;
        }
        //下付き吹出し
        if( yp == -1 ){
            yp = 440 /4 *3 -(ysize /2) -10;
            if( (yp +ysize > 420) ) yp = 420 -ysize;
        }
        //YesNoボタン位置設定
        g_iYesnoY = yp +22*iStrLine +8;

        if( gGlobal != null ){
            //低速マシン用の先行描画
            drawJapaneseFrameSub( gGlobal, str, xp, yp, xsize, ysize, true );
            //定期描き換え用の面に描画
            paintMapAll( gGlobal, false );
            drawJapaneseFrameSub( g, str, xp, yp, xsize, ysize, false );
        } else {
            //指定面に描画
            drawJapaneseFrameSub( g, str, xp, yp, xsize, ysize, false );
        }

        return true;
    }



//////////////////////////////////////////////////
//一定時間のウェイト

    public void twait( int countWait )
    {
        //タイマ設定
        try {
            Thread.sleep( countWait );
        } catch( InterruptedException e ){
            System.err.println("110 Thread Error!");
        }
    }

    public void twait( int countWait, Graphics g )
    {
        paintMapMove( g, true, 0, 0 );
        twait( countWait );
    }



//////////////////////////////////////////////////
//バイナリ・マップデータの読み込み

    public void loadMapDataSub( String filename )
    {
        int i, j;
        int fileSize;
        InputStream fp;		//ファイル入出力
        byte cByteBuffSotck[];	//データストック用

        try{
            fp = new URL( getDocumentBase(), filename ).openStream();
            j = 0;
            while( (fileSize = fp.read( byteBuffPress, j ,FREAD_BLOCK )) != -1 ){
                j += fileSize;
                //規定最大容量を超えれば領域拡張して再試行$$
                if( (j +FREAD_BLOCK) >= (g_iBlockByteBuffPress *MEM_BLOCK) ){
                    //ストックにデータ退避
                    cByteBuffSotck = new byte[j];
                    for( i = 0 ; i < j ; ++i ) cByteBuffSotck[i] = byteBuffPress[i];
                    //領域拡張
                    ++g_iBlockByteBuffPress;
                    byteBuffPress = new byte[g_iBlockByteBuffPress *MEM_BLOCK];
                    //ストックからデータ復帰
                    for( i = 0 ; i < j ; ++i ) byteBuffPress[i] = cByteBuffSotck[i];
                }
            }
            g_iFileSize = j;
            fp.close();
        } catch( MalformedURLException e ){
            g_bIOError = true;
            System.err.println( "120 URL Error!" );
        } catch( FileNotFoundException e ){
            gFileNotFound = true;
            System.err.println( "121 File not Found!" );
        } catch( IOException e ){
            g_bIOError = true;
            System.err.println( "122 MapRead Error!" );
        } catch( SecurityException e ){
            g_bIOError = true;
            System.err.println( "123 MapRead Error!" );
        } catch( Exception e ){
            g_bIOError = true;
            System.err.println( "124 MapRead Error!" );
        }
    }


    public void loadMapData( Graphics g, String filename, boolean bLoadFlag )
    {
        //変数定義
        int i, j, k;
        int x, y;
        int data;
        int PassNumber;
        //マップ上限値
        int mapAtrMax; // = MAP_ATR_MAX;
        int objAtrMax; // = OBJECT_ATR_MAX;
        int point2 = 0;
        int iDataCharaX, iDataCharaY;
        int iVersion;
        int idNumber;

        //ファイルの読み込み
        if( bLoadFlag == true ){
            loadMapDataSub( filename );
            if( (gFileNotFound == true) || (g_bIOError == true) ) return;
            //領域確保
            g_iBlockByteBuff = g_iBlockByteBuffPress;
            byteBuff = new byte[g_iBlockByteBuff *MEM_BLOCK];
        }
        //データ解凍
        extractMapData();
        point2 = g_iPointerExtract;

        //バーション取得
        iVersion = unsignedByte( byteBuff[DATA_MAP_VERSION] );

        if( iVersion <= 29 ){
            DATA_MAP_COUNT = 3;
            DATA_OBJECT_COUNT = 4;
            DATA_CHARA_X = 5;
            DATA_CHARA_Y = 6;
            DATA_OVER_X = 18;
            DATA_OVER_Y = 19;
            DATA_ITEM = 20;
            gameoverXp = unsignedByte( byteBuff[DATA_OVER_X] );
            gameoverYp = unsignedByte( byteBuff[DATA_OVER_Y] );
            iDataCharaX = unsignedByte( byteBuff[DATA_CHARA_X] );
            iDataCharaY = unsignedByte( byteBuff[DATA_CHARA_Y] );
            g_iMapPartsMax = unsignedByte( byteBuff[DATA_MAP_COUNT] );
            g_iObjectPartsMax = unsignedByte( byteBuff[DATA_OBJECT_COUNT] );
            g_bOldMap = 1;
        } else {
            DATA_MAP_COUNT = 34;
            DATA_OBJECT_COUNT = 36;
            DATA_CHARA_X = 38;
            DATA_CHARA_Y = 40;
            DATA_OVER_X = 42;
            DATA_OVER_Y = 44;
            DATA_ITEM = 60;
            gameoverXp = unsignedByte( byteBuff[DATA_OVER_X] ) + unsignedByte( byteBuff[DATA_OVER_X+1] ) * 0x100;
            gameoverYp = unsignedByte( byteBuff[DATA_OVER_Y] ) + unsignedByte( byteBuff[DATA_OVER_Y+1] ) * 0x100;
            iDataCharaX = unsignedByte( byteBuff[DATA_CHARA_X] ) + unsignedByte( byteBuff[DATA_CHARA_X+1] ) * 0x100;
            iDataCharaY = unsignedByte( byteBuff[DATA_CHARA_Y] ) + unsignedByte( byteBuff[DATA_CHARA_Y+1] ) * 0x100;
            g_iMapPartsMax = unsignedByte( byteBuff[DATA_MAP_COUNT] ) + unsignedByte( byteBuff[DATA_MAP_COUNT+1] ) * 0x100;
            g_iObjectPartsMax = unsignedByte( byteBuff[DATA_OBJECT_COUNT] ) + unsignedByte( byteBuff[DATA_OBJECT_COUNT+1] ) * 0x100;
        }

        //データ設定
        statusEnergyMax = unsignedByte( byteBuff[DATA_STATUS_ENERGYMAX] ) + unsignedByte( byteBuff[DATA_STATUS_ENERGYMAX+1] ) * 0x100;
        statusEnergy = unsignedByte( byteBuff[DATA_STATUS_ENERGY] ) + unsignedByte( byteBuff[DATA_STATUS_ENERGY+1] ) * 0x100;
        statusStrength = unsignedByte( byteBuff[DATA_STATUS_STRENGTH] ) + unsignedByte( byteBuff[DATA_STATUS_STRENGTH+1] ) * 0x100;
        statusDefence = unsignedByte( byteBuff[DATA_STATUS_DEFENCE] ) + unsignedByte( byteBuff[DATA_STATUS_DEFENCE+1] ) * 0x100;
        statusGold = unsignedByte( byteBuff[DATA_STATUS_GOLD] ) + unsignedByte( byteBuff[DATA_STATUS_GOLD+1] ) * 0x100;
        for( i = 0 ; i < 12 ; ++i ) itemBox[i] = unsignedByte( byteBuff[DATA_ITEM +i] );
        g_iMapWidth = unsignedByte( byteBuff[DATA_MAP_SIZE] ) + unsignedByte( byteBuff[DATA_MAP_SIZE+1] ) * 0x100;
        g_iMesNumberMax = unsignedByte( byteBuff[DATA_MES_NUMBER] ) + unsignedByte( byteBuff[DATA_MES_NUMBER+1] ) * 0x100;
        //	System.out.println( "jump = " +charaX +" " +charaY );

        //下位バージョン互換
        if( iVersion < 28 ) g_iMapWidth = 71;
        else if( iVersion <= 29 ) g_iMapWidth = 101;

        //プレーヤー初期位置
        JumpPoint( iDataCharaX, iDataCharaY );

        //ポインタ初期値
        g_iPointer = 100;
        //下位バージョンからの読み込みプロテクト
        if( iVersion >= 29 ) g_iPointer = 90;

        //マップデータ領域取得$$
        if( bLoadFlag == true ){
            map = new short[g_iMapWidth][g_iMapWidth];
            mapObject = new short[g_iMapWidth +1][g_iMapWidth +1];
            QSaveMap = new short[g_iMapWidth][g_iMapWidth];
            QSaveMapObject = new short[g_iMapWidth +1][g_iMapWidth +1];
        }

        //マップデータ
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                if( iVersion <= 29 ){
                    map[x][y] = (short)unsignedByte(byteBuff[g_iPointer]);
                    ++g_iPointer;
                } else {
                    map[x][y] = (short)(unsignedByte( byteBuff[g_iPointer] ) + unsignedByte( byteBuff[g_iPointer+1] ) * 0x100);
                    g_iPointer += 2;
                }
                //範囲外のパーツ削除
                if( map[x][y] >= g_iMapPartsMax ) map[x][y] = 0;
            }
        }
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                if( iVersion <= 29 ){
                    mapObject[x][y] = (short)unsignedByte(byteBuff[g_iPointer]);
                    ++g_iPointer;
                } else {
                    mapObject[x][y] = (short)(unsignedByte( byteBuff[g_iPointer] ) + unsignedByte( byteBuff[g_iPointer+1] ) * 0x100);
                    g_iPointer += 2;
                }
                //範囲外のパーツ削除
                if( mapObject[x][y] >= g_iObjectPartsMax ) mapObject[x][y] = 0;
            }
        }
        if( iVersion <= 29 ){
            mapAtrMax = 40;
            objAtrMax = 40;
        } else {
            mapAtrMax = 60;
            objAtrMax = 60;
        }
        MAP_ATR_MAX = mapAtrMax;
        OBJECT_ATR_MAX = objAtrMax;

        //マップキャラクタ領域取得
        if( bLoadFlag == true ){
            mapAttribute = new int[g_iMapPartsMax][60];
        }
        for( i = 0 ; i < g_iMapPartsMax ; ++i ){
            for( j = 0 ; j < mapAtrMax ; ++j ){
                if( (j == ATR_CROP1) || (j == ATR_CROP2) ){
                    if( bLoadFlag == true ) mapAttribute[i][j] = 0;
                    g_iPointer += 2;
                    continue;
                }
                mapAttribute[i][j] = unsignedByte(byteBuff[g_iPointer]) + unsignedByte(byteBuff[g_iPointer+1]) * 0x100;
                g_iPointer += 2;
            }
        }

        //オブジェクトキャラクタ領域取得
        if( bLoadFlag == true ){
            objectAttribute = new int[g_iObjectPartsMax][60];
        }
        for( i = 0 ; i < g_iObjectPartsMax ; ++i ){
            for( j = 0 ; j < objAtrMax ; ++j ){
                if( (j == ATR_CROP1) || (j == ATR_CROP2) ){
                    if( bLoadFlag == true ) objectAttribute[i][j] = 0;
                    g_iPointer += 2;
                    continue;
                }
                objectAttribute[i][j] = unsignedByte(byteBuff[g_iPointer]) + unsignedByte(byteBuff[g_iPointer+1]) * 0x100;
                g_iPointer += 2;
            }
        }
        //System.out.println( "Test = " +g_iObjectPartsMax );

        //下位互換拡張キャラクタ変換$$$$
        int dataChara, dataMode;
        if( iVersion <= 29 ){
            for( j = 0 ; j < g_iMapPartsMax ; ++j ){
                for( i = 9 ; i >= 0 ; --i ){
                    dataChara = mapAttribute[j][20+i*2];
                    dataChara = dataChara & 0xff;
                    dataMode = mapAttribute[j][20+i*2];
                    dataMode = dataMode >> 8;
                    x = mapAttribute[j][20+i*2+1];
                    x = x & 0xff;
                    y = mapAttribute[j][20+i*2+1];
                    y = y >> 8;
                    if( x == 250 ) x = 9000;
                    else if( x > 100 ) x += (10000 -160);
                    if( y == 250 ) y = 9000;
                    else if( y > 100 ) y += (10000 -160);

                    mapAttribute[j][20+i*4] = dataChara;
                    mapAttribute[j][20+i*4+3] = dataMode;
                    mapAttribute[j][20+i*4+1] = x;
                    mapAttribute[j][20+i*4+2] = y;
                }
                if( mapAttribute[j][ATR_TYPE] == MAP_LOCALGATE ){
                    if( mapAttribute[j][ATR_JUMP_X] > 100 ) mapAttribute[j][ATR_JUMP_X] += (10000 -160);
                    if( mapAttribute[j][ATR_JUMP_Y] > 100 ) mapAttribute[j][ATR_JUMP_Y] += (10000 -160);
                }
            }
            for( j = 0 ; j < g_iObjectPartsMax ; ++j ){
                for( i = 9 ; i >= 0 ; --i ){
                    dataChara = objectAttribute[j][20+i*2];
                    dataChara = dataChara & 0xff;
                    dataMode = objectAttribute[j][20+i*2];
                    dataMode = dataMode >> 8;
                    x = objectAttribute[j][20+i*2+1];
                    x = x & 0xff;
                    y = objectAttribute[j][20+i*2+1];
                    y = y >> 8;
                    if( x == 250 ) x = 9000;
                    else if( x > 100 ) x += (10000 -160);
                    if( y == 250 ) y = 9000;
                    else if( y > 100 ) y += (10000 -160);

                    objectAttribute[j][20+i*4] = dataChara;
                    objectAttribute[j][20+i*4+3] = dataMode;
                    objectAttribute[j][20+i*4+1] = x;
                    objectAttribute[j][20+i*4+2] = y;
                }
                if( objectAttribute[j][ATR_TYPE] == OBJECT_LOCALGATE ){
                    if( objectAttribute[j][ATR_JUMP_X] > 100 ) objectAttribute[j][ATR_JUMP_X] += (10000 -160);
                    if( objectAttribute[j][ATR_JUMP_Y] > 100 ) objectAttribute[j][ATR_JUMP_Y] += (10000 -160);
                }
            }
        }
        //ランダムキャラクタ
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                RandomCharacter( x, y );
            }
        }
        //メッセージデータの読みだし
        g_iPointer = point2;
        for( i = 0 ; i < g_iFileSize ; ++i ) byteBuff[i] = byteBuffPress[i];

        //新暗所番号
        if( iVersion >= 30 ) worldPassword = loadMapString();
        //パーツ用メッセージ
        if( iVersion <= 29 ) g_iMesNumberMax = 400;
        strMessage = new String[g_iMesNumberMax];
        for( i = 0 ; i < g_iMesNumberMax ; ++i ){
            strMessage[i] = loadMapString();
        }
        //その他データ
        worldName = loadMapString();
        if( iVersion <= 29 ) worldPassword = loadMapString();
        else loadMapString();
        if( iVersion >= 29 ) worldPassNumber = (((parse(worldPassword) /10) -1197) /17) -2357;
        else worldPassNumber = parse( worldPassword );
        mojicgName = loadMapString();
        mapcgName = loadMapString();
        //システムメッセージ
        if( iVersion >= 30 ){
            for( i = 0 ; i < 20 ; ++i ) g_szMessageSystem[i] = loadMapString();
        } else {
            for( i = 0 ; i < 20 ; ++i ) g_szMessageSystem[i] = "";
        }
        //アイテムステータス
        arrangeItem( 0 );

        //HTMLによる初期パラメータの設定
        if( getParameter("Id") != null ){
            //暗証番号計算
            idNumber = parse( getParameter("Energy") ) *7;
            idNumber += parse( getParameter("Strength") ) *11;
            idNumber += parse( getParameter("Defence") ) *19;
            idNumber += parse( getParameter("Gold") ) *5;
            idNumber += parse( getParameter("PlayerX") ) *17;
            idNumber += parse( getParameter("PlayerY") ) *21;
            for( i = 0 ; i < 12 ; ++i ){
                if( getParameter("item"+(i+1)) != null ) idNumber += parse( getParameter("item"+(i+1)) ) *17;
            }
            idNumber = idNumber %9999;
            if( parse(worldPassword) != 0 ) idNumber *= worldPassNumber;
            idNumber = idNumber %9999;

            if( idNumber == parse(getParameter("Id")) ){
                statusEnergy = parse( getParameter("Energy") );
                statusStrength = parse( getParameter("Strength") );
                statusDefence = parse( getParameter("Defence") );
                statusGold = parse( getParameter("Gold") );
                //アイテム整理
                for( i = 0 ; i < 12 ; ++i ){
                    if( getParameter("item"+(i+1)) != null ) itemBox[i] = parse( getParameter("item"+(i+1)) );
                }
                //アイテムステータス
                arrangeItem( 0 );
                statusStrength -= itemStrength;
                statusDefence -= itemDefence;

                if( getParameter("Flag") != null ){
                    if( getParameter("Flag").equals("ON") || getParameter("Flag").equals("on") ){
                        //現在位置設定
                        charaX = parse( getParameter("PlayerX") );
                        mapX = charaX /10;
                        charaX = (charaX %10) *5;
                        charaY = parse( getParameter("PlayerY") );
                        mapY = charaY /10;
                        charaY = (charaY %10) *5;
                    }
                }
            }
        }
    }


    //バイトをインテジャーに変換
    public int unsignedByte( byte numberByte )
    {
        int number;

        number = (int)numberByte;
        if( number < 0 ) number += 0x100;
        return number;
    }


    //文字列の読み込み
    public String loadMapString()
    {
        int length;
        String str = new String();
        byte data1, data2;
        byte dataByte[] = new byte[1];

        for( length = 0 ; length < 1500 ; ++length ){
            if( (byteBuff[g_iPointer +length *2] == 0) && (byteBuff[g_iPointer +length *2 +1] == 0) ) break;
            dataByte[0] = byteBuff[g_iPointer +length *2];
            data2 = byteBuff[g_iPointer +length *2 +1];
            //文字列の取り出し
            str = (str + (new String(dataByte,data2)) );
        }
        g_iPointer += length *2 +2;

        return str;
    }


    //マップデータ展開
    public void extractMapData()
    {
        //変数定義
        int i, j, k;
        int counter, maxim;
        int checkData = 0;
        int checkDataBuff;

        g_bCompleteExtract = false;

        for( i = 0, j = 0, counter = 0 ; ; ++i ){
            if( (byteBuffPress[i] == 0) && (byteBuffPress[i+1] == 0) && (byteBuffPress[i+2] == 0) ) break;
            //数字が連続していれば解凍処理
            byteBuff[j] = byteBuffPress[i];
            ++j;
            if( byteBuffPress[i] == byteBuffPress[i+1] ){
                maxim = unsignedByte( byteBuffPress[i+2] );
                for( counter = 0 ; counter < maxim ; ++counter ){
                    byteBuff[j] = byteBuffPress[i];
                    ++j;
                }
                i += 2;
            }
            //マップサイズとパーツ数のデータから必要領域取得$$
            //最大サイズを超えそうなときは領域拡張して再試行
            if( (j +255) >= (g_iBlockByteBuff *MEM_BLOCK) ){
                ++g_iBlockByteBuff;
                byteBuff = new byte[g_iBlockByteBuff *MEM_BLOCK];
                extractMapData();
                if( g_bCompleteExtract == true ) return;
            }
        }
        System.out.println( "ExtractData = " +j +" " +i );

        //正誤番号チェック
        if( unsignedByte(byteBuff[DATA_MAP_VERSION]) >= 29 ){
            for( k = 2 ; k < j ; ++k ) checkData += (byteBuff[k] *(k %8 +1));
            //マイナス処置
            checkData = (checkData % 0x10000) & 0xffff;
            //判定
            checkDataBuff = unsignedByte(byteBuff[DATA_CHECK]) +unsignedByte(byteBuff[DATA_CHECK+1]) * 0x100;
            if( checkDataBuff != checkData ) gDataBroken = true;
            System.out.println( "buff = " +checkData +" " +checkDataBuff );
        }
        g_bCompleteExtract = true;
        g_iPointerExtract = i +3;
    }


    //ランダムキャラクタ
    public void RandomCharacter( int x, int y )
    {
        if( objectAttribute[mapObject[x][y]][ATR_TYPE] == OBJECT_RANDOM ){
            int number = random.nextInt() %10;
            if( number < 0 ) number = -number;
            mapObject[x][y] = (short)objectAttribute[mapObject[x][y]][10+number];
            if( mapObject[x][y] >= g_iObjectPartsMax ) mapObject[x][y] = 0;
        }
    }



//////////////////////////////////////////////////
//グラフィックの読み込み

    public void graphicLoading( Graphics g )
    {
        //変数定義
        int i, j, k;
        int x, y;
        int maxY = 4;
        int height;

        //マップ読み込み、メディアトラッカーに登録
        imgMap = getImage( getDocumentBase(), mapcgName );
        tracker.addImage( imgMap, 0 );

        //イメージが作成されるまで待つ
        try {
            tracker.waitForID(0);
        } catch( InterruptedException e ){
            gImageNotFound = true;
            System.err.println("100 Tracker Error!");
            return;
        } catch( Exception e ){
            gImageNotFound = true;
            System.err.println("101 waitForID Error!");
            return;
        }
        LoadingMessage( g, 3 );

        //画像の読み込みチェック
        if( (tracker.statusAll(false) & MediaTracker.ERRORED) != 0 ){
            gImageNotFound = true;
            return;
        }

        //画像高さ取得
        height = imgMap.getHeight( this );

        //クロップ番号計算
        for( i = 0 ; i < g_iMapPartsMax ; ++i ){
            x = mapAttribute[i][ATR_X] /40;
            y = mapAttribute[i][ATR_Y] /40;
            //範囲外の画像は消去
            if( y >= ((height +39) /40) ){
                mapAttribute[i][ATR_CROP1] = 0;
            } else {
                if( y > maxY ) maxY = y;
                mapAttribute[i][ATR_CROP1] = x +y *10;
            }
        }
        for( i = 0 ; i < g_iObjectPartsMax ; ++i ){
            x = objectAttribute[i][ATR_X] /40;
            y = objectAttribute[i][ATR_Y] /40;
            if( y >= ((height +39) /40) ){
                objectAttribute[i][ATR_CROP1] = 0;
            } else {
                if( y > maxY ) maxY = y;
                objectAttribute[i][ATR_CROP1] = x +y *10;
            }
            x = objectAttribute[i][ATR_X2] /40;
            y = objectAttribute[i][ATR_Y2] /40;
            if( y >= ((height +39) /40) ){
                objectAttribute[i][ATR_CROP2] = 0;
            } else {
                if( y > maxY ) maxY = y;
                objectAttribute[i][ATR_CROP2] = x +y *10;
            }
        }

        //領域取得
        if( height != -1 ) g_iCropMax = ((height -1) /40 +1) *10;
        else g_iCropMax = (maxY +1) *10;
        imgCrop = new Image[g_iCropMax];

        //画像切り分け
        for( cropID = 0 ; cropID < g_iCropMax ; ++cropID ){
            imgCrop[cropID] = createImage( new FilteredImageSource( imgMap.getSource(), new CropImageFilter((cropID %10)*40, (cropID /10) *40, 40, 40) ));
        }
        //メディアトラッカーに登録
        for( k = 0 ; k < cropID ; ++k ){
            tracker.addImage( imgCrop[k], 0 );
        }
        //イメージが作成されるまで待つ
        try {
            tracker.waitForID(0);
        } catch( InterruptedException e ){
            System.err.println("102 Tracker Error!");
        }
        LoadingMessage( g, 4 );

        //元画像解放
        imgMap.flush();

        System.out.println( "Crop ID = " +cropID +" " +height );
    }



//////////////////////////////////////////////////
//定義ウィンドウ表示

    public void judgeFontAttribute()
    {
        int iHeight;

        //フォントサイズからフォントタイプ判定
        Font fon = new Font( "TimesRoman", Font.PLAIN, 20 );
        FontMetrics fm = getFontMetrics( fon );
        iHeight = fm.getAscent();
        if( iHeight > 18 ) g_bJREFont = true;
        System.out.println( "Font = " +iHeight );
    }


    public void drawHalfString( Graphics g, String szStr, int iFontSize, int x, int y, int iXsize, int iYsize )
    {
        int x2, y2;
        int xs2, ys2;
        Font fon;

        //JavaVMとJREでフォントを変える
        if( g_bJREFont == true ) fon = new Font( "TimesRoman", Font.PLAIN, iFontSize );
        else fon = new Font( "TimesRoman", Font.BOLD, iFontSize );

        FontMetrics fm = getFontMetrics( fon );
        g.setFont( fon );
        //g.setColor( Color.black );

        xs2 = fm.stringWidth( szStr );
        ys2 = fm.getAscent();
        if( ys2 >= 18 ){
            ys2 -= 6;
            if( g_bJREFont == false ) ++ys2;
        } else if( ys2 >= 16 ) ys2 -= 4;
        else if( ys2 >= 12 ) ys2 -= 2;
        if( g_bJREFont == false ) ++ys2;

        //文字幅から表示位置決定
        x2 = x +(iXsize -xs2) /2;
        y2 = y +(iYsize +ys2) /2;
        g.setColor( new Color(160,128,96) );
        g.drawString( szStr , x2+1, y2 );
        g.drawString( szStr , x2+1, y2+1 );
        g.setColor( Color.black );
        g.drawString( szStr , x2, y2 );
        //System.out.println( x2 +" " +y2 +" " +xs2 +" " +ys2 +" " );
    }


    public void displayPushButton( int iYesno, String szStr, int iFontSize, int x, int y, int iXsize, int iYsize )
    {
        if( yesnoNumber == iYesno ){
            gBuffStatus.drawImage( imgBuffButton, 0, y, this );
            drawHalfString( gBuffStatus, szStr, iFontSize, x+3, y+3, iXsize, iYsize );
        } else {
            drawHalfString( gBuffStatus, szStr, iFontSize, x, y, iXsize, iYsize );
        }
    }


    public void displayConfigWindow( Graphics g, boolean flag, boolean flag1, boolean flag2, boolean flag3 )
    {
        //変数定義
        int x;
        int i, j, k;
        int numberCrop;
        int number;
        Font fon;
        int iAddPos[] = new int[4];

        if( flag == false ){
            g.drawImage( imgBuffStatus, 40*11, 0, this );
            return;
        }
        //コマンドウィンドウ
        if( flag3 == true ){
            //fon = new Font("Courier", Font.BOLD, 18 );
            fon = new Font("TimesRoman", Font.BOLD, 18 );
            gBuffStatus.setFont( fon );

            gBuffStatus.setColor( Color.gray );
            gBuffStatus.fillRect( 0, 0, 40*3, 40*11 );

            //枠描画
            for( i = 0 ; i < 4 ; ++i ){
                for( j = 0 ; j < 3 ; ++j ) gBuffStatus.drawImage( imgCrop[j+CROP_STFRAME], j*40, 300+i*35, this );
            }
            //押しボタン作成
            gBuffButton.setColor( Color.black );
            gBuffButton.fillRect( 0, 0, 120, 35 );
            for( j = 0 ; j < 3 ; ++j ) gBuffButton.drawImage( imgCrop[j+CROP_STFRAME], j*40+3, 0+3, this );

            //各ボタンを描画
            if( quickSaveFlag == true ) displayPushButton( YESNO_QLOAD, "Quick Load", 18, 0, 300, 40*3, 35 );
            else displayPushButton( YESNO_TEXTLOAD, "Password", 18, 0, 300, 40*3, 35 );

            displayPushButton( YESNO_QSAVE, "Quick Save", 18, 0, 300+35, 40*3, 35 );
            displayPushButton( YESNO_RESTART, "Restart Game", 16, 0, 300+2*35, 40*3, 35 );
            displayPushButton( YESNO_WWAH, "Goto WWA", 18, 0, 300+3*35, 40*3, 35 );
        }
        //ステータスウィンドウ
        if( flag1 == true ){
            //生命力上限
            if( (statusEnergyMax != 0) && (statusEnergy > statusEnergyMax) ) statusEnergy = statusEnergyMax;

            //枠描画
            for( i = 0 ; i < 4 ; ++i ){
                if( (g_iChangeStatus[i] == 0) || (g_bAnmItem == 0) ){
                    for( j = 0 ; j < 3 ; ++j ) gBuffStatus.drawImage( imgCrop[j+CROP_STFRAME], j*40, i*35, this );
                } else {
                    //押しボタン
                    gBuffStatus.drawImage( imgBuffButton, 0, i*35, this );
                }
            }
            for( i = 0 ; i < 4 ; ++i ){
                if( (g_iChangeStatus[i] > 0) && (g_bAnmItem == 1) ) iAddPos[i] = 3;
                else iAddPos[i] = 0;
            }
            //イメージの表示
            gBuffStatus.drawImage( imgCrop[CROP_ENERGY], FRAME_ENERGY_X -40*11 +iAddPos[0], FRAME_ENERGY_Y +iAddPos[0], this );
            gBuffStatus.drawImage( imgCrop[CROP_STRENGTH], FRAME_STRENGTH_X -40*11 +iAddPos[1], FRAME_STRENGTH_Y +iAddPos[1], this );
            gBuffStatus.drawImage( imgCrop[CROP_DEFENCE], FRAME_DEFENCE_X -40*11 +iAddPos[2], FRAME_DEFENCE_Y +iAddPos[2], this );
            gBuffStatus.drawImage( imgCrop[CROP_GOLD], FRAME_GOLD_X -40*11 +iAddPos[3], FRAME_GOLD_Y +iAddPos[3], this );

            //パラメータ表示
            if( statusEnergy < 0 ) statusEnergy = 0;
            drawHalfString( gBuffStatus, String.valueOf(statusEnergy), 22, 38 +iAddPos[0], 0 +iAddPos[0], 40*3-38, 35 );
            drawHalfString( gBuffStatus, String.valueOf(statusStrength +itemStrength), 22, 38 +iAddPos[1], 35 +iAddPos[1], 40*3-38, 35 );
            drawHalfString( gBuffStatus, String.valueOf(statusDefence +itemDefence), 22, 38 +iAddPos[2], 35*2 +iAddPos[2], 40*3-38, 35 );
            drawHalfString( gBuffStatus, String.valueOf(statusGold), 22, 38 +iAddPos[3], 35*3 +iAddPos[3], 40*3-38, 35 );
        }
        //アイテムウィンドウ
        int iPlus;
        int iBox;
        if( flag2 == true ){
            for( i = 0 ; i < 4 ; ++i ){
                for( j = 0 ; j < 3 ; ++j ){
                    iBox = i*3+j;
                    gBuffStatus.drawImage( imgCrop[CROP_ITEMFRAME], j*40, 140+i*40, this );

                    if( (g_iAnmItemCount >= 1) && (g_iAnmItemCount <= (15 +g_iAnmItemCountAdd)) && (g_iAnmItemBox == iBox) && (g_iAnmItemOld == 0) ) continue;

                    if( (itemBox[iBox] != 0) || ((iBox == itemboxBuff) && (g_iUseItem > 0)) ){
                        if( (iBox == itemboxBuff) && (g_iUseItem > 0) ) numberCrop = objectAttribute[g_iUseItem][ATR_CROP1];
                        else numberCrop = objectAttribute[itemBox[iBox]][ATR_CROP1];
                        //アイテムアニメ中
                        if( (g_iAnmItemBox == iBox) && (g_iAnmItemOld != 0) && (g_iAnmItemCount != 0) ) numberCrop = objectAttribute[g_iAnmItemOld][ATR_CROP1];

                        //アイテム描画
                        if( (objectAttribute[itemBox[iBox]][ATR_MODE] == 0) && (iBox != itemboxBuff) ){
                            gBuffStatus.drawImage( imgCrop[numberCrop], j*40, 140+i*40, this );
                        }
                        //使用型アイテムは別描画
                        else {
                            iPlus = 0;
                            if( (g_iUseItem > 0) && (iBox == itemboxBuff) ){
                                //押しボタン作成
                                gBuffMap.setColor( Color.black );
                                gBuffMap.fillRect( 0, 0, 40, 40 );
                                iPlus = 3;
                            }
                            gBuffMap.drawImage( imgCrop[CROP_ITEMFRAME], iPlus, iPlus, this );

                            //赤枠描画
                            if( g_iImgClickItem != 0 ){
                                //ユーザー指定枠
                                gBuffMap.drawImage( imgCrop[g_iImgClickItem], iPlus, iPlus, this );
                            } else {
                                for( k = 0 ; k < 5 ; ++k ){
                                    if( (k %2) == 1 ) gBuffMap.setColor( Color.white );
                                    else gBuffMap.setColor( Color.red );
                                    gBuffMap.drawRect( k+1+iPlus, k+1+iPlus, 37-k*2, 37-k*2 );
                                }
                            }
                            gBuffMap.drawImage( imgCrop[numberCrop], iPlus, iPlus, this );
                            gBuffStatus.drawImage( imgBuffMap, j*40, 140+i*40, this );
                        }
                    }
                }
            }
        }
        g.drawImage( imgBuffStatus, 40*11, 0, this );
    }



//////////////////////////////////////////////////
//アイテムの並べかえ

    public void arrangeItem( int data )
    {
        //変数定義
        int i;
        int dataOld;
        int iOldItemStrength, iOldItemDefence;

        for( i = 0 ; i < 12 ; ++i ){
            if( itemBox[i] == 0 ) break;
        }
        if( (i == 12) && (data != 0) && (objectAttribute[data][ATR_NUMBER] == 0) ) return;

        if( data != 0 ){
            if( objectAttribute[data][ATR_NUMBER] != 0 ){
                dataOld = itemBox[objectAttribute[data][ATR_NUMBER] -1];
                itemBox[objectAttribute[data][ATR_NUMBER] -1] = data;

                if( objectAttribute[dataOld][ATR_NUMBER] != objectAttribute[data][ATR_NUMBER] ){
                    for( i = 0 ; i < 12 ; ++i ){
                        if( itemBox[i] == 0 ){
                            itemBox[i] = dataOld;
                            break;
                        }
                    }
                }
            } else {
                for( i = 0 ; i < 12 ; ++i ){
                    if( itemBox[i] == 0 ){
                        itemBox[i] = data;
                        break;
                    }
                }
            }
        }
        //ステータス再計算
        itemStrength = 0;
        itemDefence = 0;
        for( i = 0 ; i < 12 ; ++i ){
            if( itemBox[i] != 0 ){
                itemStrength += objectAttribute[itemBox[i]][ATR_STRENGTH];
                itemDefence += objectAttribute[itemBox[i]][ATR_DEFENCE];
            }
        }
    }



//////////////////////////////////////////////////
//モンスターとの戦闘処理

    public void attackMonster( Graphics g, int Xpoint, int Ypoint )
    {
        //変数定義
        int i, j;
        int x, y;
        int MonsterData = mapObject[Ypoint][Xpoint];

        x = (charaX /5) *40;
        y = (charaY /5) *40;

        if( attackFlagTurn == true ){
            //効果音
            if( (soundFlag == true) && (g_iAttackTurn <= 40) && ((g_iAttackTurn == 0) || (TimerCount > 10)) ) playAudio( 3 );

            //キヤラクタ描画
            cropID = mapAttribute[map[playerY][playerX]][ATR_CROP1];
            gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
            if( g_bDelPlayer == 0 ) gBuffMap.drawImage( imgCrop[cropIDchara], 0, 0, this );
            paintFrame2( gBuffMap, x /40, y /40, 0, 0 );
            g.drawImage( imgBuffMap, x, y, this );

            //モンスター描画
            cropID = mapAttribute[map[Ypoint][Xpoint]][ATR_CROP1];
            gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
            attackMonsterSub2( g, Xpoint, Ypoint, true );
            g.drawImage( imgBuffMap, (Xpoint -mapX *10) *40, (Ypoint -mapY *10) *40, this );

            //プレーヤーの攻撃
            if( (statusStrength +itemStrength > monsterDefence) || (statusDefence +itemDefence < monsterStrength) || (g_bAttackJudgeMes == false) ){
                if(monsterEnergy > (statusStrength +itemStrength -monsterDefence)){
                    if( (statusStrength +itemStrength) > monsterDefence ) monsterEnergy = monsterEnergy -(statusStrength +itemStrength -monsterDefence);
                } else {
                    //プレーヤーの勝ち
                    monsterEnergy = 0;
                    mdata = mapObject[Ypoint][Xpoint];
                    mapFlagAll = true;
                    mapFlagErase = false;
                    waitCounterLast = 200;
                    //拡張モンスターキャラクタの場合
                    messageFlag = true;
                    strNumber = objectAttribute[mdata][ATR_STRING];
                    //キャラクタ出現
                    appearChara( g, Xpoint, Ypoint, 0 );
                    statusGold += monsterGold;
                    flagDisplayStatus = true;
                    //所持アイテム出現
                    if( mapObject[Ypoint][Xpoint] == mdata ){
                        mapObject[Ypoint][Xpoint] = (short)objectAttribute[mdata][ATR_ITEM];
                        displayCharacter( g, Xpoint, Ypoint );
                    }
                    attackFlag = false;
                    movingSkip = 1;
                    //サウンド
                    if((objectAttribute[mdata][ATR_SOUND] != 0) && (soundFlag == true)){
                        waitCounterLast = 0;
                        twait( 200 );
                        playAudio( objectAttribute[mdata][ATR_SOUND] );
                    }
                }
            } else {
                mapFlagAll = true;
                mapFlagErase = false;
                messageFlag = true;
                strNumber = 1;
                strMessage[strNumber] = "相手の防御能力が高すぎる！";
                attackFlag = false;
                movingSkip = 1;
                waitCounterLast = 200;
            }
            displayMonsterStatus( g, MonsterData );
            attackFlagTurn = false;

            if( (g_iAttackTurn > 40) || (TimerCount <= 10) ) twait( 20 );
            else twait( 120 );
            ++g_iAttackTurn;
        }
        else if( attackFlagTurn == false ){
            //効果音
            //if( (soundFlag == true) && (g_iAttackTurn <= 40) ) audio[4].play();

            //モンスター描画
            cropID = mapAttribute[map[Ypoint][Xpoint]][ATR_CROP1];
            gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
            attackMonsterSub2( g, Xpoint, Ypoint, false );
            g.drawImage( imgBuffMap, (Xpoint -mapX *10) *40, (Ypoint -mapY *10) *40, this );

            //キヤラクタ描画
            cropID = mapAttribute[map[playerY][playerX]][ATR_CROP1];
            gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
            if( g_bDelPlayer == 0 ) gBuffMap.drawImage( imgCrop[cropIDchara], 0, 0, this );
            gBuffMap.drawImage( imgCrop[CROP_BOM], 0, 0, this);
            paintFrame2( gBuffMap, x /40, y /40, 0, 0 );
            g.drawImage( imgBuffMap, x, y, this );

            //モンスターの攻撃
            if( monsterStrength > statusDefence +itemDefence ){
                if(statusEnergy > (monsterStrength-statusDefence -itemDefence)){
                    statusEnergy = statusEnergy - (monsterStrength -statusDefence -itemDefence);
                } else {
                    //モンスターの勝ち
                    JumpPoint( gameoverXp, gameoverYp );

                    statusEnergy = 0;
                    mapFlagAll = true;
                    mapFlagErase = true;
                    attackFlag = false;
                    twait( 800 );
                }
            }
            attackFlagTurn = true;

            if( (g_iAttackTurn > 40) || (TimerCount <= 10) ) twait( 20 );
            else twait( 120 );
            ++g_iAttackTurn;
        }
        //ステータス表示
        displayConfigWindow( g, true, true, true, false );
    }


    public void attackMonsterSub2( Graphics g, int Xpoint, int Ypoint, boolean flag )
    {
        //変数定義
        int x, y;

        cropID = objectAttribute[mapObject[Ypoint][Xpoint]][ATR_CROP1];
        x = (Xpoint -mapX *10) *40;
        y = (Ypoint -mapY *10) *40;
        gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
        if( flag == true ) gBuffMap.drawImage( imgCrop[CROP_BOM], 0, 0, this);

        //フレーム描画
        paintFrame2( gBuffMap, x /40, y /40, 0, 0 );
    }


    //モンスターのステータス表示
    public void displayMonsterStatus( Graphics g, int MonsterData )
    {
        int Ytop;
        if( charaY /5 >= 5 ) Ytop = 80;
        else Ytop = 300;

        gBuffBattle.setColor( new Color(96,96,96) );
        gBuffBattle.fillRect( 0, 0, 340, 60 );
        gBuffBattle.setColor( Color.white );
        gBuffBattle.fillRoundRect( 2, 2, 340-4, 60-4, 20, 20 );

        //フォント設定
        Font fon = new Font( "Courier", Font.PLAIN, 18 );
        gBuffBattle.setFont( fon );

        cropID = objectAttribute[MonsterData][ATR_CROP1];
        gBuffBattle.drawImage( imgCrop[cropID], 20, 10, this );

        drawStringBold( gBuffBattle, "生命力  "+monsterEnergy, Color.black, Color.gray, 80, 26 );
        drawStringBold( gBuffBattle, "攻撃力  "+monsterStrength +"  防御力  "+monsterDefence, Color.black, Color.gray, 80, 46 );
        g.drawImage( imgBuffBattle, 50, Ytop, this );
    }



//////////////////////////////////////////////////
//指定位置のマップを表示

    public void displayCharacter( Graphics g, int Xpoint, int Ypoint )
    {
        //変数定義
        int i, j;
        int x, y;
        int mapData;

        //ランダムキャラクタ
        RandomCharacter( Ypoint, Xpoint );

        if( !((Xpoint >= mapX *10) && (Xpoint < mapX *10 +11) && (Ypoint >= mapY *10) && (Ypoint < mapY *10 +11)) ) return;
        if( mapFlagAll == true ) return;
        if( g_bSkipPaint == true ) return;

        //背景色で塗りつぶし
        gBuffMap.setColor( Color.gray );
        gBuffMap.fillRect( 0, 0, 40, 40 );

        //背景描画
        mapData = map[Ypoint][Xpoint];
        cropID = mapAttribute[mapData][ATR_CROP1];
        gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );

        //プレーヤー描画
        if( (g_bDelPlayer == 0) && (Xpoint == (charaX /5 +mapX *10)) && (Ypoint == (charaY /5 +mapY *10)) ) gBuffMap.drawImage( imgCrop[cropIDchara],0,0,this );

        x = (Xpoint %10) *40;
        if( (Xpoint /10) != mapX ) x = 40 *10;
        y = (Ypoint %10) *40;
        if( (Ypoint /10) != mapY ) y = 40 *10;

        //オブジェクト描画
        mapData = mapObject[Ypoint][Xpoint];
        //プレーヤーと取得型アイテムが重なる場合は描画しない
        if( (mapData != 0) && (CheckNoDrawParts(mapData,Xpoint,playerX,Ypoint,playerY) == false) ){
            cropID = objectAttribute[mapData][ATR_CROP1];
            gBuffMap.drawImage( imgCrop[cropID], 0, 0, this );
        }
        //フレーム描画
        paintFrame2( gBuffMap, x /40, y /40, 0, 0 );
        g.drawImage( imgBuffMap, x, y, this );
    }



//////////////////////////////////////////////////
//スコア表示

    public void displayScore( Graphics g )
    {
        //変数
        int i;

        Font fon = new Font("Monospaced", Font.BOLD, 22 );
        g.setFont( fon );

        g.setColor( Color.white );
        g.fillRoundRect( 40*2, 30, 40*7, 40, 8, 8 );

        g.setColor( Color.black );
        g.drawString( "Score", 40*3, 60 -2 );
        g.drawString( String.valueOf( score ), 40*6, 60 -2 );
    }



//////////////////////////////////////////////////
//拡張出現キャラクタ

    public void appearChara( Graphics g, int Xpoint, int Ypoint, int flagNumber )
    {
        appearChara( g, Xpoint, Ypoint, flagNumber, 0 );
    }

    public void appearChara( Graphics g, int Xpoint, int Ypoint, int flagNumber, int iMapNumber )
    {
        int i;
        int dataChara;
        int dataMode;
        int x, y;
        int mapNumber;
        boolean flag;
        int iMin = 0;
        int iMax = 10;

        if( flagNumber == 1 ){
            mapNumber = map[Ypoint][Xpoint];
            flag = false;
        } else if( flagNumber == 2 ){
            mapNumber = itemBox[itemboxBuff];
            flag = true;
        } else {
            mapNumber = mapObject[Ypoint][Xpoint];
            flag = true;
        }
        if( iMapNumber != 0 ) mapNumber = iMapNumber;

        //二者択一の場合
        if( flagNumber == 3 ){
            iMax = 5;
        } else if( flagNumber == 4 ){
            iMin = 5;
            iMax = 10;
        }

        //拡張キャラクタ・データコンバート
        for( i = iMin ; i < iMax ; ++i ){
            if( flag == true ) dataChara = objectAttribute[mapNumber][20+i*4];
            else dataChara = mapAttribute[mapNumber][20+i*4];

            if( flag == true ) dataMode = objectAttribute[mapNumber][20+i*4+3];
            else dataMode = mapAttribute[mapNumber][20+i*4+3];

            //ＸＹ座標系
            if( flag == true ) x = objectAttribute[mapNumber][20+i*4+1];
            else x = mapAttribute[mapNumber][20+i*4+1];
            //プレーヤー位置指定
            if( x == 9000 ) x = playerX;
            else if( x > 9000 ) x = x - 10000 + Xpoint;

            if( flag == true ) y = objectAttribute[mapNumber][20+i*4+2];
            else y = mapAttribute[mapNumber][20+i*4+2];
            //プレーヤー位置指定
            if( y == 9000 ) y = playerY;
            else if( y > 9000 ) y = y - 10000 + Ypoint;

            if( (x == 0) && (y == 0) ) continue;

            if( (x >= 0) && (x <= g_iMapWidth-1) && (y >= 0) && (y <= g_iMapWidth-1) ){
                if( dataMode == 0 ){
                    if( (dataChara >= g_iObjectPartsMax) ) continue;
                    mapObject[y][x] = (short)dataChara;
                    displayCharacter( g, x, y );
                } else if( dataMode == 1 ){
                    if( (dataChara >= g_iMapPartsMax) ) continue;
                    map[y][x] = (short)dataChara;
                    displayCharacter( g, x, y );
                }
            }
        }
    }



//////////////////////////////////////////////////
//ＵＲＬジャンプ

    String oldString;

    public void jumpURL( String str, boolean returnFlag )
    {
        int i;
        URL url;
        String urlStr;
        String target = "";
        boolean flag = false;
        String encodeName = URLEncoder.encode( worldName );
        int idNumber;
        int idNumberB;

        StringTokenizer strtoken = new StringTokenizer( str );
        urlStr = strtoken.nextToken();
        if( strtoken.hasMoreTokens() ){
            target = strtoken.nextToken();
        }
        if( (returnFlag == true) && (!strMessage[5].equals("BLANK")) ){
            yesnoNumber = YESNO_URLGATE;
            yesnoFlag = true;
            return;
        }
        //CGI判定用
        String adr = new String( urlStr.toCharArray(),urlStr.length() -3,3 );

        //暗証番号計算
        idNumber = statusEnergy *7;
        idNumber += (statusStrength +itemStrength) *11;
        idNumber += (statusDefence +itemDefence) *19;
        idNumber += statusGold *5;
        idNumber += playerX *17;
        idNumber += playerY *21;
        for( i = 0 ; i < 12 ; ++i ) idNumber += itemBox[i] *17;
        idNumber = idNumber %9999;
        if( parse(worldPassword) != 0 ) idNumber *= worldPassNumber;
        idNumber = idNumber %9999;
        //移動回数新規追加
        idNumberB = g_iStep *231;
        for( i = 0 ; i < 100 ; ++i ) idNumberB += g_iValiable[i] *(i +5);
        if( parse(worldPassword) != 0 ) idNumberB *= worldPassNumber;
        idNumberB = idNumber %9999;

        //拡張送信
        if( target.equals("EXPAND") ){
            urlStr = urlStr +"&";
            flag = true;
        } else if( adr.equals("cgi") ){
            urlStr = urlStr +"?";
            flag = true;
        }
        if( flag == true ){
            //２重送信の防止
            //if( urlStr.equals(oldString) ) return;
            oldString = urlStr;
            //CGIで送信するデータ
            urlStr = urlStr +"HP=" +statusEnergy +"&AT=" +(statusStrength +itemStrength) +"&DF=" +(statusDefence +itemDefence) +"&GD=" +statusGold +"&PX=" +playerX +"&PY=" +playerY +"&ID=" +idNumber +"&WNAME=" +encodeName;
            for( i = 0 ; i < 12 ; ++i ) urlStr = urlStr +"&ITEM" +(i+1) +"=" +itemBox[i];
            if( !target.equals("EXPAND") ) urlStr = urlStr +"&FLAG=ON" +"&DAT=" +getParameter("paramMapName");
            urlStr = urlStr +"&STEP=" +g_iStep +"&IDB=" +idNumberB;
        }
        System.out.println( urlStr );

        //メモリ解放
        if( g_bUseUrlJump == true ) System.gc();

        try{
            urlJumpFlag = true;
            g_bUseUrlJump = true;
            //ターゲットの有無別にＵＲＬジャンプ
            if( target.equals("BLANK") || target.equals("EXPAND") ){
                url = new URL( getDocumentBase(), urlStr );
                getAppletContext().showDocument( url );
            } else {
                if( g_bPopup == true ) target = "wwawindow";
                if( target.equals("") ){
                    url = new URL( getDocumentBase(), urlStr );
                    getAppletContext().showDocument( url );
                } else {
                    url = new URL( getDocumentBase(), urlStr );
                    getAppletContext().showDocument( url, target );
                }
            }
        } catch( MalformedURLException e ){
            System.err.println( "140 URL Error!" );
        }
    }



//////////////////////////////////////////////////
//メッセージの表示

    public void DisplayMessage( String str, boolean bCenter )
    {
        messageFlag = true;
        strNumber = 1;
        strMessage[strNumber] = str;
        g_bDisplayCenter = bCenter;
    }



//////////////////////////////////////////////////
//データの一時保存

    public void QuickSave()
    {
        int i, j;
        int x, y;
        int point = 0;

        QSaveParameter[point] = charaX;
        QSaveParameter[++point] = mapX;
        QSaveParameter[++point] = charaY;
        QSaveParameter[++point] = mapY;
        QSaveParameter[++point] = statusEnergy;
        QSaveParameter[++point] = statusStrength;
        QSaveParameter[++point] = statusDefence;
        QSaveParameter[++point] = statusGold;
        QSaveParameter[++point] = gameoverXp;
        QSaveParameter[++point] = gameoverYp;
        QSaveParameter[++point] = itemStrength;
        QSaveParameter[++point] = itemDefence;
        QSaveParameter[++point] = g_iImgCharaCrop;
        QSaveParameter[++point] = CROP_YES;
        QSaveParameter[++point] = g_bSaveStop;
        QSaveParameter[++point] = g_bDefault;
        QSaveParameter[++point] = g_bOldMap;
        QSaveParameter[++point] = g_iStep;
        QSaveParameter[++point] = g_iMusicNumber;
        QSaveParameter[++point] = statusEnergyMax;
        for( i = 0 ; i < 12 ; ++i ) QSaveParameter[++point] = itemBox[i];
        QSaveParameter[++point] = CROP_ENERGY;
        QSaveParameter[++point] = CROP_STRENGTH;
        QSaveParameter[++point] = CROP_DEFENCE;
        QSaveParameter[++point] = CROP_GOLD;
        QSaveParameter[++point] = CROP_BOM;
        QSaveParameter[++point] = CROP_STFRAME;
        QSaveParameter[++point] = CROP_ITEMFRAME;
        QSaveParameter[++point] = CROP_MAINFRAME;
        QSaveParameter[++point] = g_bDelPlayer;
        QSaveParameter[++point] = g_iImgClickItem;
        QSaveParameter[++point] = g_iEffWait;
        QSaveParameter[++point] = g_bAnmItem;
        for( i = 0 ; i < 4 ; ++i ) QSaveParameter[++point] = g_iEffCrop[i];

        //変数用
        for( i = 0 ; i < 100 ; ++i ) QSaveParameter[++point] = g_iValiable[i];

        //マップデータ
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                QSaveMap[x][y] = map[x][y];
                QSaveMapObject[x][y] = mapObject[x][y];
            }
        }
    }


    public void QuickLoad()
    {
        int i, j;
        int x, y;
        int point = 0;

        //背景音楽停止
        playAudio( 99 );
        g_bRestPlayer = true;

        charaX = QSaveParameter[point];
        charaX = charaX -(charaX %5);
        mapX = QSaveParameter[++point];
        charaY = QSaveParameter[++point];
        charaY = charaY -(charaY %5);
        mapY = QSaveParameter[++point];
        statusEnergy = QSaveParameter[++point];
        statusStrength = QSaveParameter[++point];
        statusDefence = QSaveParameter[++point];
        statusGold = QSaveParameter[++point];
        gameoverXp = QSaveParameter[++point];
        gameoverYp = QSaveParameter[++point];
        itemStrength = QSaveParameter[++point];
        itemDefence = QSaveParameter[++point];
        g_iImgCharaCrop = QSaveParameter[++point];
        SetYesNoCrop( QSaveParameter[++point] );
        g_bSaveStop = QSaveParameter[++point];
        g_bDefault = QSaveParameter[++point];
        g_bOldMap = QSaveParameter[++point];
        g_iStep = QSaveParameter[++point];
        g_iMusicNumber = QSaveParameter[++point];
        statusEnergyMax = QSaveParameter[++point];
        for( i = 0 ; i < 12 ; ++i ) itemBox[i] = QSaveParameter[++point];
        CROP_ENERGY = QSaveParameter[++point];
        CROP_STRENGTH = QSaveParameter[++point];
        CROP_DEFENCE = QSaveParameter[++point];
        CROP_GOLD = QSaveParameter[++point];
        CROP_BOM = QSaveParameter[++point];
        CROP_STFRAME = QSaveParameter[++point];
        CROP_ITEMFRAME = QSaveParameter[++point];
        CROP_MAINFRAME = QSaveParameter[++point];
        g_bDelPlayer = QSaveParameter[++point];
        g_iImgClickItem = QSaveParameter[++point];
        g_iEffWait = QSaveParameter[++point];
        g_bAnmItem = QSaveParameter[++point];
        for( i = 0 ; i < 4 ; ++i ) g_iEffCrop[i] = QSaveParameter[++point];
        //変数用
        for( i = 0 ; i < 100 ; ++i ) g_iValiable[i] = QSaveParameter[++point];

        //背景音楽再開
        playAudio( 100 );

        //マップデータ
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                map[x][y] = QSaveMap[x][y];
                mapObject[x][y] = QSaveMapObject[x][y];
            }
        }
        SetDirectPlayer( 2 );

        //画面書き換え
        g_bFadeBlack = true;
    }



//////////////////////////////////////////////////
// ゲームオーバー

    public void GameOver( Graphics g )
    {
        displayConfigWindow( g, true, true, true, false );

        mapX = gameoverXp /10;
        charaX = (gameoverXp %10) *5;
        mapY = gameoverYp /10;
        charaY = (gameoverYp %10) *5;
        statusEnergy = 0;

        mapFlagAll = true;
        mapFlagErase = true;
        g_bGameOver = false;

        twait( 500 );
    }



//////////////////////////////////////////////////
//動き回るキャラクタの設定

    public void WonderingChara()
    {
        int i, j;
        int x, y;
        int xp, yp;
        int xc, yc;
        boolean FlagMatrix[][] = new boolean[13][13];
        boolean flag;
        Random random = new Random();
        int xold, yold;

        //現在位置がジャンプゲートならスキップ
        if( mapAttribute[map[playerY][playerX]][ATR_TYPE] == MAP_LOCALGATE ) return;
        if( objectAttribute[mapObject[playerY][playerX]][ATR_TYPE] == OBJECT_LOCALGATE ) return;

        //移動回数加算
        if( g_iTurnSkip == 0 ) ++g_iStep;
        g_bRestPlayer = false;

        for( x = 0 ; x < 13 ; ++x ){
            for( y = 0 ; y < 13 ; ++y ){
                FlagMatrix[x][y] = false;
            }
        }
        for( x = -1 ; x < 12 ; ++x ){
            for( y = -1 ; y < 12 ; ++y ){
                if( FlagMatrix[x+1][y+1] == true ) continue;
                if( (mapX*10+x < 0) || (mapY*10+y < 0) || (mapX*10+x > g_iMapWidth-1) || (mapY*10+y > g_iMapWidth-1) ) continue;

                mdata = mapObject[y +mapY *10][x +mapX *10];
                if( (mdata != 0) && (objectAttribute[mdata][ATR_MOVE] > 0) && (objectAttribute[mdata][ATR_MOVE] <= 3) && (objectAttribute[mdata][ATR_TYPE] != OBJECT_LOCALGATE) ){
                    flag = true;

                    //プレーヤー位置
                    xp = charaX /5;
                    yp = charaY /5;
                    if( charaX %5 == 1 ) ++xp;
                    if( charaY %5 == 1 ) ++yp;

                    //斜め移動
                    xc = x;
                    if( objectAttribute[mdata][ATR_MOVE] == 1 ){
                        if( x > xp ) xc = x -1;
                        else if( x < xp ) xc = x +1;
                    } else if( objectAttribute[mdata][ATR_MOVE] == 2 ){
                        if( x > xp ) xc = x +1;
                        else if( x < xp ) xc = x -1;
                    }
                    yc = y;
                    if( objectAttribute[mdata][ATR_MOVE] == 1 ){
                        if( y > yp ) yc = y -1;
                        else if( y < yp ) yc = y +1;
                    } else if( objectAttribute[mdata][ATR_MOVE] == 2 ){
                        if( y > yp ) yc = y +1;
                        else if( y < yp ) yc = y -1;
                    }
                    xold = xc;
                    yold = yc;
                    //斜め移動不可の場合
                    if( WonderingCharaSub(xc,yc,xp,yp) == false ){
                        if( (((moveDirect == 2) || (moveDirect == 8)) && (objectAttribute[mdata][ATR_TYPE] == OBJECT_MONSTER))
                                || (((moveDirect == 4) || (moveDirect == 6)) && (objectAttribute[mdata][ATR_TYPE] != OBJECT_MONSTER)) ){
                            if( WonderingCharaSub(x,yc,xp,yp) == false ){
                                if( WonderingCharaSub(xc,y,xp,yp) == false ) flag = false;
                                else yc = y;
                            } else {
                                xc = x;
                            }
                        } else {
                            if( WonderingCharaSub(xc,y,xp,yp) == false ){
                                if( WonderingCharaSub(x,yc,xp,yp) == false ) flag = false;
                                else xc = x;
                            } else {
                                yc = y;
                            }
                        }
                    }
                    //追尾属性で前方不可なら斜め前方、次点に横方向、逃げる属性で後方不可なら斜め後方、次点に横方向
                    if( (objectAttribute[mdata][ATR_MOVE] == 1) || (objectAttribute[mdata][ATR_MOVE] == 2) ){
                        j = 1;
                        if( objectAttribute[mdata][ATR_MOVE] == 1 ) j = -1;
                        if( (xp != x) && (flag == false) ){
                            xc = xold;
                            yc = y +1 *j;
                            if( WonderingCharaSub(xc,yc,xp,yp) == false ){
                                yc = y -1 *j;
                                //横方向検索
                                if( WonderingCharaSub(xc,yc,xp,yp) == false ){
                                    xc = x;
                                    yc = y +1 *j;
                                    if( WonderingCharaSub(xc,yc,xp,yp) == false ) yc = y -1 *j;
                                }
                            }
                        }
                        flag = WonderingCharaSub( xc, yc, xp, yp );
                        //Ｙ方向検索
                        if( (yp != y) &&  (flag == false) ){
                            yc = yold;
                            xc = x +1 *j;
                            if( WonderingCharaSub(xc,yc,xp,yp) == false ){
                                xc = x -1 *j;
                                //横方向検索
                                if( WonderingCharaSub(xc,yc,xp,yp) == false ){
                                    yc = y;
                                    xc = x +1 *j;
                                    if( WonderingCharaSub(xc,yc,xp,yp) == false ) xc = x -1 *j;
                                }
                            }
                        }
                        flag = WonderingCharaSub( xc, yc, xp, yp );
                    }
                    //ランダムに移動
                    if( flag == false ){
                        for( i = 0 ; i < 50 ; ++i ){
                            xc = x +random.nextInt() %2;
                            //フラグとキャラクタセット
                            if( flag == true ){
                                if( (x >= 0) && (y >= 0) && (x < 11) && (y < 11) ) mapFlag[x][y] = true;
                                if( (xc >= 0) && (yc >= 0) && (xc < 11) && (yc < 11) ) mapFlag[xc][yc] = true;
                            yc = y +random.nextInt() %2;
                            if( WonderingCharaSub(xc,yc,xp,yp) == true ){
                                flag = true;
                                break;
                            }
                        }
                    }
                        //物体設置
                        if( (xc >= -1) && (yc >= -1) && (xc < 12) && (yc < 12) ) FlagMatrix[xc+1][yc+1] = true;
                        mapObject[y +mapY *10][x +mapX *10] = 0;
                        mapObject[mapY *10 +yc][mapX *10 +xc] = (short)mdata;
                        //移動アニメ用フラグ
                        if( (xc >= -1) && (yc >= -1) && (xc < 12) && (yc < 12) ){
                            if( xc > x ) g_iMapObjMove[yc+1][xc+1][0] = -5;
                            else if( xc < x ) g_iMapObjMove[yc+1][xc+1][0] = 5;
                            if( yc > y ) g_iMapObjMove[yc+1][xc+1][1] = -5;
                            else if( yc < y ) g_iMapObjMove[yc+1][xc+1][1] = 5;
                        }
                    }
                }
            }
        }
    }

    public boolean WonderingCharaSub( int xc, int yc, int xp, int yp )
    {
        if( (mapX*10+xc < 0) || (mapY*10+yc < 0) || (mapX*10+xc > g_iMapWidth-1) || (mapY*10+yc > g_iMapWidth-1) ) return false;

        if( (mapAttribute[map[mapY *10 +yc][mapX* 10 +xc]][ATR_TYPE] == MAP_WALL)
                || (mapObject[mapY *10 +yc][mapX *10 +xc] != 0)
                || ((map[mapY *10 +yc][mapX *10 +xc] == 0) && (g_bOldMap == 0))
                || ((xp == xc) && (yp == yc)) ){
            return false;
        }
        return true;
    }



//////////////////////////////////////////////////
//モンスターの能力値表示

    public void DisplayMonsterData( Graphics g )
    {
        int i;
        int x, y;
        int objectData;
        int damage;
        int energyM;
        int attack, attackM;
        int turn;
        int number = 0;
        int oldData[] = new int[8];		//すでに計算したモンスター

        gBuff.setColor( Color.white );
        gBuff.fillRect( 0, 0, 440, 440 );
        //フォント設定
        Font fon = new Font( "Courier", Font.PLAIN, 16 );
        gBuff.setFont( fon );

        for( y = 0 ; y < 11 ; ++y ){
            for( x = 0 ; x < 11 ; ++x ){
                objectData = mapObject[y+mapY*10][x+mapX*10];
                if( objectAttribute[objectData][ATR_TYPE] == OBJECT_MONSTER ){
                    for( i = 0 ; i < number ; ++i ){
                        if( oldData[i] == objectData ) break;
                    }
                    if( (i != number) && (oldData[i] == objectData) ) continue;

                    monsterEnergy = objectAttribute[objectData][ATR_ENERGY];
                    monsterStrength = objectAttribute[objectData][ATR_STRENGTH];
                    monsterDefence = objectAttribute[objectData][ATR_DEFENCE];
                    energyM = monsterEnergy;

                    attack = (statusStrength +itemStrength) - monsterDefence;
                    attackM = monsterStrength -(statusDefence +itemDefence);
                    if( attackM < 0 ) attackM = 0;
                    turn = 0;
                    damage = 0;

                    if( attack > 0 ){
                        while( true ){
                            ++turn;
                            energyM -= attack;
                            if( energyM <= 0 ) break;
                            damage += attackM;
                            if( turn > 10000 ) break;
                        }
                    }
                    //ダメージ表示
                    drawStringBold( gBuff, "生命力  "+monsterEnergy, Color.black, Color.gray, 80, 40+number*50 );
                    drawStringBold( gBuff, "攻撃力  "+monsterStrength, Color.black, Color.gray, 200, 40+number*50 );
                    drawStringBold( gBuff, "防御力  "+monsterDefence, Color.black, Color.gray, 80, 60+number*50 );

                    if( attack > 0 ) drawStringBold( gBuff, "予測ダメージ  " +damage, Color.red, new Color(255,128,128), 200, 60+number*50 );
                    else drawStringBold( gBuff, "攻撃無効", Color.red, new Color(255,128,128), 200, 60+number*50 );

                    //イメージ表示
                    cropID = objectAttribute[objectData][ATR_CROP1];
                    gBuff.drawImage( imgCrop[cropID], 20, 25 +number*50, this );

                    oldData[number] = objectData;
                    ++number;
                }
                if( number >= 8 ) break;
            }
            if( number >= 8 ) break;
        }
        if( number == 0 ){
            displayMonsterFlag = false;
            inputKey = true;
        } else {
            g.drawImage( imgBuff, 0, 0, this );
        }
    }



//////////////////////////////////////////////////
//保存用パスワードの表示

    Frame g_frameWin;
    Checkbox g_cbSavePassword = new Checkbox( "このウィンドウを閉じるにはここを押してください。" );
    Checkbox g_cbLoadPassword = new Checkbox( "データを入力したらここを押してください。" );
    boolean g_bOpenFrameWin = false;
    boolean g_bOpenLoadPass = false;
    TextArea g_taPassText = new TextArea();

    public void DisplaySavePassword( String szPassText )
    {
        TextArea taPassText = new TextArea();
        TextArea taMessage = new TextArea( "下のボックスのテキストがデータ復帰用のパスワードになっています。\nコピーしてメモ帳などのテキストエディタに貼り付けて保存してください。\n「Ctrl+C」でコピー、「Ctrl+V」で貼り付けできます。\nコピーが効かなかったり、選択状態になっていないときは、\n「Ctrl+Home」でテキストの先頭にカーソルをもってきて、\n「Shift+Ctrl+End」で再度全体を選択してからコピーしてください。\nパスワードは通常、数十行〜数百行になります。\n先頭に「Ａ」最後尾に「Ｚ」の文字があることを確認してください。", 8,100 );
        taMessage.setEditable( false );

        g_frameWin = new Frame( "データ復帰用パスワード" );
        g_frameWin.setSize( 480, 440 );

        g_frameWin.setLayout( new BorderLayout() );
        g_frameWin.add( "Center", taPassText );
        g_frameWin.add( "North", taMessage );
        g_frameWin.add( "South", g_cbSavePassword );
        g_frameWin.show();

        g_cbSavePassword.setState( false );
        taPassText.setText(szPassText);
        taPassText.selectAll();

        urlJumpFlag = true;
        g_bOpenFrameWin = true;
    }


    public void InputLoadPassword()
    {
        TextArea taMessage = new TextArea( "下のボックスに前回のプレイで取得した復帰用パスワードを入力してください。\nテキストは、Ctrtl+Cでコピー、Ctrl+Vで貼り付けできます。\nテキストの先頭に「Ａ」最後尾に「Ｚ」の文字があることを確認してください。\n作成者がマップの内容を変更した場合は\nそれ以前に取得したパスワードは使えなくなります。", 5,100 );
        taMessage.setEditable( false );

        g_frameWin = new Frame( "データ復帰用パスワード入力" );
        g_frameWin.setSize( 480, 440 );

        g_frameWin.setLayout( new BorderLayout() );
        g_frameWin.add( "Center", g_taPassText );
        g_frameWin.add( "North", taMessage );
        g_frameWin.add( "South", g_cbLoadPassword );
        g_frameWin.show();

        g_cbLoadPassword.setState( false );
        g_taPassText.setText("");

        urlJumpFlag = true;
        g_bOpenLoadPass = true;
        g_bOpenFrameWin = true;
    }



//////////////////////////////////////////////////
//マップデータ圧縮

    int ReturnPartsCheckNumber()
    {
        int i, j;
        int iCheckData = 0;

        for( i = 0 ; i < 90 ; ++i ){
            if( (i != DATA_CHECK_PARTS) && (i != DATA_CHECK_PARTS +1) && (i != DATA_CHECK) && (i != DATA_CHECK +1) ) iCheckData += unsignedByte( PressData[i] );
        }
        for( i = 0 ; i < g_iMapPartsMax ; ++i ){
            for( j = 0 ; j < MAP_ATR_MAX ; ++j ){
                iCheckData += mapAttribute[i][j];
                iCheckData %= 32000;
            }
        }
        for( i = 0 ; i < g_iObjectPartsMax ; ++i ){
            for( j = 0 ; j < OBJECT_ATR_MAX ; ++j ){
                iCheckData += objectAttribute[i][j];
                iCheckData %= 32000;
            }
        }
        System.out.println( "check_data = " +iCheckData );

        return iCheckData;
    }


    void PressDataSub( int iAtr, int iData, boolean bMax )
    {
        if( (bMax == true) && (iData > 65000) ) iData = 65000;
        PressData[iAtr] = (byte)iData;
        PressData[iAtr +1] = (byte)(iData >> 8);
    }


    void PressSaveMapData()
    {
        int i, j;
        int pointer = 0;
        int counter;
        int x, y;
        int length;
        int iCheckData;
        byte BuffConvert[]; // = new byte[MEM_BLOCK];
        byte MapData[]; // = new byte[MEM_BLOCK];

        MapData = new byte[g_iMapWidth *g_iMapWidth *4];
        if( PressData == null ){
            PressData = new byte[g_iMapWidth *g_iMapWidth*4 +1000];
        }

        PressDataSub( EX_DATA_STATUS_ENERGYMAX, statusEnergyMax, true );
        PressDataSub( DATA_STATUS_ENERGY, statusEnergy, true );
        PressDataSub( DATA_STATUS_STRENGTH, statusStrength, true );
        PressDataSub( DATA_STATUS_DEFENCE, statusDefence, true );
        PressDataSub( DATA_STATUS_GOLD, statusGold, true );
        PressDataSub( EX_DATA_CHARA_X, playerX, false );
        PressDataSub( EX_DATA_CHARA_Y, playerY, false );
        PressDataSub( EX_DATA_OVER_X, gameoverXp, false );
        PressDataSub( EX_DATA_OVER_Y, gameoverYp, false );
        for( i = 0 ; i < 12 ; ++i ) PressDataSub( (EX_DATA_ITEM +i*2), itemBox[i], false );
        PressDataSub( DATA_IMG_CHARA_CROP, g_iImgCharaCrop, false );
        PressDataSub( DATA_IMG_YESNO_CROP, CROP_YES, false );
        PressDataSub( DATA_SAVE_STOP, g_bSaveStop, false );
        PressDataSub( DATA_FLAG_DEFAULT, g_bDefault, false );
        PressDataSub( DATA_FLAG_OLDMAP, g_bOldMap, false );
        PressDataSub( DATA_STEP, g_iStep, false );
        PressDataSub( DATA_MUSIC, g_iMusicNumber, false );
        PressDataSub( DATA_CROP, CROP_ENERGY, false );
        PressDataSub( DATA_CROP +2, CROP_STRENGTH, false );
        PressDataSub( DATA_CROP +4, CROP_DEFENCE, false );
        PressDataSub( DATA_CROP +6, CROP_GOLD, false );
        PressDataSub( DATA_CROP +8, CROP_BOM, false );
        PressDataSub( DATA_CROP +10, CROP_STFRAME, false );
        PressDataSub( DATA_CROP +12, CROP_ITEMFRAME, false );
        PressDataSub( DATA_CROP +14, CROP_MAINFRAME, false );
        PressDataSub( DATA_FLAG_DELP, g_bDelPlayer, false );
        PressDataSub( DATA_IMG_CLICK, g_iImgClickItem, false );
        PressDataSub( DATA_EFFECT, g_iEffWait, false );
        for( i = 0 ; i < 4 ; ++i ) PressDataSub( DATA_EFFECT +2+i*2, g_iEffCrop[i], false );
        PressDataSub( DATA_ANMITEM, g_bAnmItem, false );

        //変数用
        for( i = 0 ; i < 100 ; ++i ) PressDataSub( DATA_VALIABLE +i*2, g_iValiable[i], true );

        //マップデータ
        pointer = 380;
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                PressData[pointer] = (byte)map[x][y];
                PressData[pointer +1] = (byte)(map[x][y] >> 8);
                pointer += 2;
            }
        }
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                PressData[pointer] = (byte)mapObject[x][y];
                PressData[pointer +1] = (byte)(mapObject[x][y] >> 8);
                pointer += 2;
            }
        }

        //パーツデータの整合値作成
        PressDataSub( DATA_CHECK_PARTS, ReturnPartsCheckNumber(), false );

        //正誤番号設定
        for( i = 2, iCheckData = 0 ; i < pointer ; ++i ){
            iCheckData += unsignedByte(PressData[i]) *(i %18 +1);
            iCheckData %= 32000;
        }
        PressDataSub( DATA_CHECK, iCheckData, false );

        //マップデータ圧縮
        for( i = 0, j = 0, counter = 0 ; i < pointer ; ++i ){
            if( PressData[i] == PressData[i+1] ){
                ++counter;
                if( (counter == 0xff) || (i +2 > pointer) ){
                    MapData[j] = PressData[i];
                    MapData[j+1] = PressData[i];
                    MapData[j+2] = (byte)counter;
                    j += 3;
                    ++i;
                    counter = 0;
                }
            } else {
                if( counter == 0 ){
                    MapData[j] = PressData[i];
                    ++j;
                } else {
                    MapData[j] = PressData[i];
                    MapData[j+1] = PressData[i];
                    MapData[j+2] = (byte)counter;
                    j += 3;
                }
                counter = 0;
            }
        }
        MapData[j] = 0;
        MapData[j+1] = 0;
        MapData[j+2] = 0;
        length = j+2;
        System.out.println( "save_pointer = " +pointer +" " +length );

        //領域確保
        BuffConvert = new byte[length*2+2000];
        //始端
        BuffConvert[0] = 'A';
        //２バイト系のテキストに変換
        for( i = 0, j = 1, counter = 0 ; i < length ; ++i ){
            BuffConvert[j] = (byte)((MapData[i] & 0x0f) +'B');
            BuffConvert[j+1] = (byte)(((MapData[i] >> 4) & 0x0f) +'B');
            j += 2;
            //改行処理
            ++counter;
            if( counter >= 250 ){
                BuffConvert[j] = '\n';
                counter = 0;
                ++j;
            }
        }
        //終端
        BuffConvert[j] = 'Z';
        BuffConvert[j+1] = '\n';

        DisplaySavePassword( new String(BuffConvert,0,j+2) );
    }



//////////////////////////////////////////////////
//マップデータ展開

    public void SetYesNoCrop( int iImgYesnoCrop )
    {
        CROP_YES = iImgYesnoCrop;
        CROP_NO = iImgYesnoCrop +1;
        CROP_YES2 = iImgYesnoCrop +2;
        CROP_NO2 = iImgYesnoCrop +3;
    }


    public int ExtractDataSub( int iData )
    {
        return (unsignedByte(PressData[iData]) +unsignedByte(PressData[iData+1]) *0x100);
    }


    public void ExtractLoadMapData()
    {
        //変数定義
        int i, j, k;
        int x, y;
        int counter, maxim;
        boolean bStart = false;
        int pointer = 0;
        int iDataCharaX, iDataCharaY;
        int iCheckData, iCheckDataEx;
        boolean bTerm = false;
        int length;
        byte BuffConvert[];
        byte MapData[];

        g_bOpenLoadPass = false;
        if( PressData == null ){
            PressData = new byte[g_iMapWidth *g_iMapWidth*4 +1000];
        }

        //テキストをバイトデータに変換
        String szPassText = g_taPassText.getText();
        BuffConvert = szPassText.getBytes();
        length = szPassText.length();
        if( length == 0 ) return;
        MapData = new byte[length/2];

        //１バイト系データに変換
        for( i = 0, j = 0 ; j < length ; ){
            //終端検索
            if( BuffConvert[j] == 'Z' ){
                bTerm = true;
                break;
            }
            if( bStart == false ){
                //始端検索
                if( BuffConvert[j] == 'A' ) bStart = true;
                ++j;
                if( j > 100 ) break;
            } else {
                //改行は無視
                if( (BuffConvert[j] == '\n') || (BuffConvert[j] == '\r') ){
                    ++j;
                    continue;
                }
                //データ変換
                if( j >= (length-1) ) break;
                MapData[i] = (byte)((BuffConvert[j] -'B') +((BuffConvert[j+1] -'B') << 4));
                ++i;
                j += 2;
            }
        }

        if( (bTerm == false) || (bStart == false) ){
            DisplayMessage( "パスワードが正常ではありません。\nパスワードテキストの先頭が「Ａ」\n最後尾が「Ｚ」になっているか\n確認してください。", true );
            return;
        }

        //データ解凍
        for( i = 0, j = 0, counter = 0 ; ; ++i ){
            if( (MapData[i] == 0) && (MapData[i+1] == 0) && (MapData[i+2] == 0) ) break;
            //数字が連続していれば解凍処理
            PressData[j] = MapData[i];
            ++j;
            if( MapData[i] == MapData[i+1] ){
                maxim = unsignedByte( MapData[i+2] );
                for( counter = 0 ; counter < maxim ; ++counter ){
                    PressData[j] = MapData[i];
                    ++j;
                }
                i += 2;
            }
        }
        System.out.println( "load_pointer = " +j +" " +i );

        pointer = j-1;
        //正誤番号判定
        iCheckDataEx = ExtractDataSub( DATA_CHECK );
        for( i = 2, iCheckData = 0 ; i < pointer ; ++i ){
            iCheckData += unsignedByte(PressData[i]) *(i %18 +1);
            iCheckData %= 32000;
        }
        if( iCheckData != iCheckDataEx ){
            DisplayMessage( "パスワードが正常ではありません。\nパスワードの一部が欠損していないかなどを確認してください。", true );
            return;
        }

        //パーツデータの整合値判定
        iCheckDataEx = ExtractDataSub( DATA_CHECK_PARTS );
        if( ReturnPartsCheckNumber() != iCheckDataEx ){
            DisplayMessage( "このパスワードはこのマップでは\n使用できません。\nマップの内容が前回から変更されているか、または別のマップのパスワードを使用しています。", true );
            return;
        }

        //背景音楽停止
        playAudio( 99 );
        g_bRestPlayer = true;

        statusEnergyMax = ExtractDataSub( EX_DATA_STATUS_ENERGYMAX );
        statusEnergy = ExtractDataSub( DATA_STATUS_ENERGY );
        statusStrength = ExtractDataSub( DATA_STATUS_STRENGTH );
        statusDefence = ExtractDataSub( DATA_STATUS_DEFENCE );
        statusGold = ExtractDataSub( DATA_STATUS_GOLD );
        gameoverXp = ExtractDataSub( EX_DATA_OVER_X );
        gameoverYp = ExtractDataSub( EX_DATA_OVER_Y );
        for( i = 0 ; i < 12 ; ++i ) itemBox[i] = ExtractDataSub( (EX_DATA_ITEM +i*2) );
        arrangeItem( 0 );
        iDataCharaX = ExtractDataSub( EX_DATA_CHARA_X );
        iDataCharaY = ExtractDataSub( EX_DATA_CHARA_Y );
        JumpPoint( iDataCharaX, iDataCharaY );
        g_iImgCharaCrop = ExtractDataSub( DATA_IMG_CHARA_CROP );
        //moveDirect = 2;
        SetDirectPlayer( 2 );
        SetYesNoCrop( ExtractDataSub(DATA_IMG_YESNO_CROP) );
        g_bSaveStop = ExtractDataSub( DATA_SAVE_STOP );
        g_bDefault = ExtractDataSub( DATA_FLAG_DEFAULT );
        g_bOldMap = ExtractDataSub( DATA_FLAG_OLDMAP );
        g_iStep = ExtractDataSub( DATA_STEP );
        g_iMusicNumber = ExtractDataSub( DATA_MUSIC );
        CROP_ENERGY = ExtractDataSub( DATA_CROP );
        CROP_STRENGTH = ExtractDataSub( DATA_CROP +2 );
        CROP_DEFENCE = ExtractDataSub( DATA_CROP +4 );
        CROP_GOLD = ExtractDataSub( DATA_CROP +6 );
        CROP_BOM = ExtractDataSub( DATA_CROP +8 );
        CROP_STFRAME = ExtractDataSub( DATA_CROP +10 );
        CROP_ITEMFRAME = ExtractDataSub( DATA_CROP +12 );
        CROP_MAINFRAME = ExtractDataSub( DATA_CROP +14 );
        g_bDelPlayer = ExtractDataSub( DATA_FLAG_DELP );
        g_iImgClickItem = ExtractDataSub( DATA_IMG_CLICK );
        g_iEffWait = ExtractDataSub( DATA_EFFECT );
        for( i = 0 ; i < 4 ; ++i ) g_iEffCrop[i] = ExtractDataSub( DATA_EFFECT +2+i*2 );
        g_bAnmItem = ExtractDataSub( DATA_ANMITEM );
        //変数用
        for( i = 0 ; i < 100 ; ++i ) g_iValiable[i] = ExtractDataSub( DATA_VALIABLE +i*2 );

        //背景音楽再開
        playAudio( 100 );

        pointer = 380;
        //マップデータ展開
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                map[x][y] = (short)(unsignedByte(PressData[pointer]) +unsignedByte(PressData[pointer+1]) *0x100);
                pointer += 2;
            }
        }
        for( x = 0 ; x < g_iMapWidth ; ++x ){
            for( y = 0 ; y < g_iMapWidth ; ++y ){
                mapObject[x][y] = (short)(unsignedByte(PressData[pointer]) +unsignedByte(PressData[pointer+1]) *0x100);
                pointer += 2;
            }
        }

        //画面書き換え
        g_bFadeBlack = true;
    }



//////////////////////////////////////////////////
// 太文字で描画

    public void drawStringBold( Graphics g, String szStr, Color colorA, Color colorB, int x, int y )
    {
        g.setColor( colorB );
        g.drawString( szStr, x+1, y );
        g.setColor( colorA );
        g.drawString( szStr, x, y );
    }

}
