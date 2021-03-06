import lime.graphics.opengl.GL;
import lime.ui.Window;
import lime.ui.Touch;
import lime.ui.KeyCode;
import lime.graphics.Renderer;

typedef LimeInput = {% CLASS com.jtransc.media.limelibgdx.LimeInput %};

class HaxeLimeGdxApplication extends lime.app.Application {
    static public var instance:HaxeLimeGdxApplication;
    static public var app:{% CLASS com.jtransc.media.limelibgdx.LimeApplication %};
    static public var initializingListener:Bool = false;
    static public var initializedListener:Bool = false;

    static private var DEBUG = false;

	#if flash
	static public var sprite: lime.graphics.FlashRenderContext; // flash.display.Sprite
	static public var context3D: flash.display3D.Context3D;
	#else
	static public var gl: lime.graphics.GLRenderContext;
	#end

	public function getWidth() return window.width;
	public function getHeight() return window.height;

    static public function convertByteBuffer(buf:{% CLASS java.nio.ByteBuffer %}, size = -1) {
        var len = buf.{% METHOD java.nio.ByteBuffer:limit:()I %}();
        var out = new lime.utils.UInt8Array(len);
        for (n in 0 ... len) out[n] = buf.{% METHOD java.nio.ByteBuffer:get:(I)B %}(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        return out;
    }

    static public function convertShortBuffer(buf:{% CLASS java.nio.ShortBuffer %}, size = -1) {
        var len = buf.{% METHOD java.nio.ShortBuffer:limit:()I %}();
        var out = new lime.utils.Int16Array(len);
        for (n in 0 ... len) out[n] = buf.{% METHOD java.nio.ShortBuffer:get:(I)S %}(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        return out;
    }

    static public function convertIntBuffer(buf:{% CLASS java.nio.IntBuffer %}, size = -1) {
        var len = buf.{% METHOD java.nio.IntBuffer:limit:()I %}();
        var out = new lime.utils.Int32Array(len);
        for (n in 0 ... len) out[n] = buf.{% METHOD java.nio.IntBuffer:get:(I)I %}(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        return out;
    }

    static public function convertFloatBuffer(buf:{% CLASS java.nio.FloatBuffer %}, size = -1) {
        var len = buf.{% METHOD java.nio.FloatBuffer:limit:()I %}();
        var out = new lime.utils.Float32Array(len);
        for (n in 0 ... len) out[n] = buf.{% METHOD java.nio.FloatBuffer:get:(I)F %}(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        //trace(out);
        return out;
    }

    static public function convertBuffer(buf:{% CLASS java.nio.Buffer %}, size:Int = -1):lime.utils.ArrayBufferView {
        if (Std.is(buf, {% CLASS java.nio.ByteBuffer %})) return convertByteBuffer(cast(buf, {% CLASS java.nio.ByteBuffer %}));
        if (Std.is(buf, {% CLASS java.nio.ShortBuffer %})) return convertShortBuffer(cast(buf, {% CLASS java.nio.ShortBuffer %}));
        if (Std.is(buf, {% CLASS java.nio.IntBuffer %})) return convertIntBuffer(cast(buf, {% CLASS java.nio.IntBuffer %}));
        if (Std.is(buf, {% CLASS java.nio.FloatBuffer %})) return convertFloatBuffer(cast(buf, {% CLASS java.nio.FloatBuffer %}));
		throw 'Not implemented convertBuffer!';
    }

    static public function convertIntArray(buf:JA_I, offset:Int, size:Int):lime.utils.Int32Array {
        var len = buf.length;
        var out = new lime.utils.Int32Array(len);
        for (n in 0 ... len) out[n] = buf.get(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        return out;
    }

    static public function convertFloatArray(buf:JA_F, offset:Int, size:Int):lime.utils.Float32Array {
        var len = buf.length;
        var out = new lime.utils.Float32Array(len);
        for (n in 0 ... len) out[n] = buf.get(n);
        if (DEBUG) trace([for (n in 0 ... out.length) out[n]]);
        return out;
    }

    static public function loopInit(init: Void -> Void) {
    }

    static public function loopLoop(update: Void -> Void, render: Void -> Void) {
    }

	public var preloadComplete:Bool = false;
    public override function onPreloadComplete():Void {
    	preloadComplete = true;
        //switch (renderer.context) {
        //	case FLASH(sprite): #if flash initializeFlash(sprite); #end
        //	case OPENGL (gl):
        //	default:
        //	throw "Unsupported render context";
        //}
    }

    public override function render(renderer:lime.graphics.Renderer) {
        super.render(renderer);
        if (app != null) {
            if (!initializingListener && preloadComplete) {
                initializingListener = true;
                switch (renderer.context) {
					#if flash
					case FLASH(sprite): HaxeLimeGdxApplication.sprite = sprite;
					#else
					case OPENGL(gl): HaxeLimeGdxApplication.gl = gl;
					#end
					default: throw 'Not supported renderer $renderer';
				}

				#if flash
					var sprite = HaxeLimeGdxApplication.sprite;
					var stage = sprite.stage;
					var stage3D = stage.stage3Ds[0];

					stage3D.addEventListener(flash.events.Event.CONTEXT3D_CREATE, function(e) {
                    	context3D = stage3D.context3D;
						if (!initializedListener) {
							app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:create %}();
							initializedListener = true;
						}
					});
                    stage3D.requestContext3D();
				#else
	                app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:create %}();
					initializedListener = true;
				#end
            }
            if (initializedListener) {
            	app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:render %}();
            }
        }
    }

    public override function update(deltaTime:Int) {
        super.update(deltaTime);
    }

    private function isReady() return app != null && initializedListener;

    public override function onWindowActivate(window:Window):Void {
        if (isReady()) app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:onResumed %}();
    }

    public override function onWindowDeactivate(window:Window):Void {
        if (isReady()) app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:onPaused %}();
    }

    public override function onWindowClose (window:Window):Void {
        if (isReady()) app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:onDisposed %}();
    }

    public function new() {
        super();
        HaxeLimeGdxApplication.instance = this;
        addModule(new JTranscModule());
    }

	public override function onWindowResize(window:Window, width:Int, height:Int):Void {
		if (isReady()) app.{% METHOD com.jtransc.media.limelibgdx.LimeApplication:resized:(II)V %}(width, height);
	}

	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////

	//static var program;
	//static var vertexBuffer;
	//
	//static public function testInit() {
	//	program = createProgram(
	//		"attribute vec3 aVertexPosition; void main(void) { gl_Position = vec4(aVertexPosition, 1.0); }",
	//		"void main(void) { gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);}"
	//	);
	//	vertexBuffer = createTriangle();
	//}
	//
	//static private function createTriangle() {
	//	var vertexBuffer = gl.createBuffer();
	//	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	//
	//	var vertices = new lime.utils.Float32Array(9);
	//	vertices[0] =  0;
	//	vertices[1] =  1;
	//	vertices[2] =  0;
	//	vertices[3] = -1;
	//	vertices[4] = -1;
	//	vertices[5] =  0;
	//	vertices[6] =  1;
	//	vertices[7] = -1;
	//	vertices[8] =  0;
	//
	//	gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
	//	return vertexBuffer;
	//}
	//
	//static private function createProgram(vertexCode:String, fragmentCode:String) {
	//	var program = gl.createProgram();
	//	gl.attachShader(program, createShader(gl.VERTEX_SHADER, vertexCode));
	//	gl.attachShader(program, createShader(gl.FRAGMENT_SHADER, fragmentCode));
	//	gl.linkProgram(program);
	//	trace(gl.getProgramInfoLog(program));
	//	return program;
	//}
	//
	//static private function createShader(opcode:Int, code:String) {
	//	var shader = gl.createShader(opcode);
	//	gl.shaderSource(shader, code);
	//	gl.compileShader(shader);
	//	return shader;
	//}
	//
	//static public function testFrame() {
	//	gl.enable(gl.BLEND);
	//	gl.viewport(0, 0, 1280, 720);
	//
	//	gl.clearColor(1, 0, 1, 1);
	//	gl.clearStencil(0);
	//	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	//
	//	gl.disable(gl.SCISSOR_TEST);
	//	gl.disable(gl.STENCIL_TEST);
	//	gl.depthMask(false);
	//	gl.colorMask(true, true, true, true);
	//	gl.stencilOp(gl.KEEP, gl.KEEP, gl.KEEP);
	//	gl.stencilFunc(gl.EQUAL, 0x00, 0x00);
	//	gl.stencilMask(0x00);
	//
	//	var pos = gl.getAttribLocation(program, "aVertexPosition");
	//
	//	gl.enableVertexAttribArray(pos);
	//	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	//
	//	gl.vertexAttribPointer(
	//		pos,               // attribute 0. No particular reason for 0, but must match the layout in the shader.
	//		3,                 // size
	//		gl.FLOAT,     // opcode
	//		false,             // normalized?
	//		0,                 // stride
	//		0                  // array buffer offset
	//	);
	//	gl.useProgram(program);
	//	gl.drawArrays(gl.TRIANGLES, 0, 3); // Starting from vertex 0; 3 vertices total -> 1 triangle
	//	gl.disableVertexAttribArray(pos);
	//}
}

class JTranscModule extends lime.app.Module {
    override public function onMouseUp (window:Window, x:Float, y:Float, button:Int):Void {
    	LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onMouseUp %}(x, y, button);
    }
    override public function onMouseDown (window:Window, x:Float, y:Float, button:Int):Void {
    	LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onMouseDown %}(x, y, button);
    }
    override public function onMouseMove (window:Window, x:Float, y:Float):Void {
    	LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onMouseMove %}(x, y);
    }


	override public function onKeyDown(window:Window, keyCode:KeyCode, modifier:lime.ui.KeyModifier):Void {
		LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onKeyDown %}(keyCode, modifier);
	}
	override public function onKeyUp(window:Window, keyCode:KeyCode, modifier:lime.ui.KeyModifier):Void {
		LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onKeyUp %}(keyCode, modifier);
	}


	override public function onTouchEnd(touch:Touch):Void {
		LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onTouchEnd %}(touch.id, touch.x, touch.y);
	}

	override public function onTouchMove(touch:Touch):Void {
		LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onTouchMove %}(touch.id, touch.x, touch.y);
	}

   	override public function onTouchStart(touch:Touch):Void {
		LimeInput.{% METHOD com.jtransc.media.limelibgdx.LimeInput:lime_onTouchStart %}(touch.id, touch.x, touch.y);
   	}
}