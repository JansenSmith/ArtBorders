import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Transform
import eu.mihosoft.vrl.v3d.svg.SVGExporter
import eu.mihosoft.vrl.v3d.Polygon
import eu.mihosoft.vrl.v3d.Slice

def name
def piece_width
def piece_height
def border_width
def border_thickness
def do_rabbet
if(args==null){
	name = "pandemonium"
	piece_width = 236
	piece_height = 155.8
	border_width = 7
	border_thickness = 4
	do_rabbet = false
	CSG borders_rabbet_false, borders_rabbet_true
	ArrayList<Object> borders_rabbet_false_params = new ArrayList<Object>()
	borders_rabbet_false_params.add(name)
	borders_rabbet_false_params.add(piece_width)
	borders_rabbet_false_params.add(piece_height)
	borders_rabbet_false_params.add(border_width)
	borders_rabbet_false_params.add(border_thickness)
	borders_rabbet_false_params.add(do_rabbet)
	borders_rabbet_false =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  borders_rabbet_false_params // send the factory the name param
	)
	do_rabbet = true
	ArrayList<Object> borders_rabbet_true_params = new ArrayList<Object>()
	borders_rabbet_true_params.add(name)
	borders_rabbet_true_params.add(piece_width)
	borders_rabbet_true_params.add(piece_height)
	borders_rabbet_true_params.add(border_width)
	borders_rabbet_true_params.add(border_thickness)
	borders_rabbet_true_params.add(do_rabbet)
	borders_rabbet_true =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  borders_rabbet_true_params // send the factory the name param
	)//.movex(10).movey(10)
		
	CSG cutout = new Cube(piece_width, piece_height, border_thickness).toCSG()
					.toXMin()
					.toYMin()
					.toZMin()
	CSG ziggurat = borders_rabbet_true.union(cutout).union(cutout.movez(cutout.totalZ/2))
	ziggurat = ziggurat.addSlicePlane(new Transform().movez(ziggurat.totalZ - 0.1))
					.addSlicePlane(new Transform().movez(0.1).movey(ziggurat.totalY + 10))
					.addSlicePlane(new Transform().movez(ziggurat.totalZ/2).movey(2*(ziggurat.totalY + 10)))
	ziggurat = ziggurat.addExportFormat("svg")
	
	
	
	borders_rabbet_false = borders_rabbet_false.setColor(javafx.scene.paint.Color.DARKGRAY)
		.setName(name+"_base")
		.addAssemblyStep(0, new Transform())
		.setIsWireFrame(true)
		.setManufacturing({ toMfg ->
			return toMfg
					//.rotx(180)// fix the orientation
					//.toZMin()//move it down to the flat surface
		})
		
	borders_rabbet_true = borders_rabbet_true.setColor(javafx.scene.paint.Color.DARKRED)
		.setName(name+"_base_rabbeted")
		.addAssemblyStep(0, new Transform())
		.setManufacturing({ toMfg ->
			return toMfg
					//.rotx(180)// fix the orientation
					//.toZMin()//move it down to the flat surface
		})
	
	ziggurat = ziggurat.setColor(javafx.scene.paint.Color.MAGENTA)
		.setName(name+"_ziggurat")
//		.addAssemblyStep(0, new Transform())
//		.setManufacturing({ toMfg ->
//			return toMfg
//					//.rotx(180)// fix the orientation
//					//.toZMin()//move it down to the flat surface
//		})
		//.addSlicePlane(new Transform().movez(-ziggurat.totalZ+0.1))
	
	printSingleExample(piece_width, piece_height)
	//ret = [borders_rabbet_true.movez(-border_thickness/2-0.1+10)]
	ret = ziggurat//.roty(45)//.movez(-ziggurat.totalZ+0.1)
	return ret
} else if (args.size() >= 5 && args.get(5)) {
	name = args.get(0)
	piece_width = args.get(1)
	piece_height = args.get(2)
	border_width = args.get(3)
	border_thickness = args.get(4)
	do_rabbet = args.get(5)
	println "Nesting constructor to create a rabbeted border with these parameters: name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness+
				", do_rabbet = "+do_rabbet
	CSG border_internal, border_external
	do_rabbet = false
	def border_internal_width = border_width / 2
	def border_internal_thickness = border_thickness
	ArrayList<Object> border_internal_params = new ArrayList<Object>()
	border_internal_params.add(name)
	border_internal_params.add(piece_width)
	border_internal_params.add(piece_height)
	border_internal_params.add(border_internal_width)
	border_internal_params.add(border_internal_thickness)
	border_internal_params.add(do_rabbet)
	border_internal =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  border_internal_params // send the factory the name param
	)
	def border_external_width = border_width
	def border_external_thickness = border_thickness / 2
	ArrayList<Object> border_external_params = new ArrayList<Object>()
	border_external_params.add(name)
	border_external_params.add(piece_width)
	border_external_params.add(piece_height)
	border_external_params.add(border_external_width)
	border_external_params.add(border_external_thickness)
	border_external_params.add(do_rabbet)
	border_external =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  border_external_params // send the factory the name param
	)
	border = border_internal.union(border_external)
	ret = border
	return ret //[ret, cutout]
} else {
	name = args.get(0)
	piece_width = args.get(1)
	piece_height = args.get(2)
	border_width = args.get(3)
	border_thickness = args.get(4)
	do_rabbet = args.get(5)
	println "Parameters sent to border constructor: name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness+
				", do_rabbet = "+do_rabbet
}

def width = piece_width // + 2*border_width
def height = piece_height // + 2*border_width

CSG bottom = new Cube(width, border_width, border_thickness).toCSG()
					.toZMin()
					.toYMax()
					.toXMin()
					//.movex(-border_width)
CSG top = bottom.toYMin().movey(piece_height)

CSG left = new Cube(border_width, height, border_thickness).toCSG()
					.toZMin()
					.toXMax()
					.toYMin()
					//.movey(-border_width)
CSG right = left.toXMin().movex(piece_width)
CSG borders = bottom.union(top).union(left).union(right)

CSG rounding = new Cylinder(border_width, border_thickness, (int)32).toCSG()
					.toZMin()

borders = borders.union(rounding)
					.union(rounding.movex(width))
					.union(rounding.movey(height))
					.union(rounding.movex(width).movey(height))
					
CSG cutout = new Cube(width, height, border_thickness).toCSG()
					.toXMin()
					.toYMin()
					.toZMin()
					
borders = borders.difference(cutout)

CSG ret = borders
		
return ret

/**
 * Calculates reasonable backboard dimensions for framing
 * @param piece_width Width of artwork in mm
 * @param piece_height Height of artwork in mm
 * @return Map with backboard_x and backboard_y dimensions in mm
 */
def calculateBackboardSize(double piece_width, double piece_height) {
	
	// Frame parameters (all in mm)
	def frame_width = 20.0           // Frame molding width on each side
	def cutting_margin = 6.0         // Laser cutting margin on each side
	
	// Mat border calculation - dynamic based on artwork size
	def base_mat_border = 50.0       // Minimum mat border
	def size_factor = Math.max(piece_width, piece_height) / 200.0  // Scale factor
	def mat_border = Math.max(base_mat_border, base_mat_border * size_factor)
	
	// Optional: bottom-weighted mat (traditional framing)
	def mat_top = mat_border
	def mat_bottom = mat_border * 1.15  // 15% larger bottom border
	def mat_left = mat_border
	def mat_right = mat_border
	
	// Calculate total backboard dimensions
	def backboard_x = piece_width + mat_left + mat_right + (frame_width * 2) + (cutting_margin * 2)
	def backboard_y = piece_height + mat_top + mat_bottom + (frame_width * 2) + (cutting_margin * 2)
	
	// Round up to nearest mm for practical cutting
	backboard_x = Math.ceil(backboard_x)
	backboard_y = Math.ceil(backboard_y)
	
	return [
		x: backboard_x,
		y: backboard_y,
		mat_border: mat_border,
		frame_info: [
			piece_width: piece_width,
			piece_height: piece_height,
			mat_top: mat_top,
			mat_bottom: mat_bottom,
			mat_left: mat_left,
			mat_right: mat_right,
			frame_width: frame_width,
			cutting_margin: cutting_margin
		]
	]
}

/**
 * Prints detailed info for a single backboard calculation
 */
def printSingleExample(double piece_width, double piece_height) {
    def backboard = calculateBackboardSize(piece_width, piece_height)
    println "Artwork: ${backboard.frame_info.piece_width}mm x ${backboard.frame_info.piece_height}mm"
    println "Backboard needed: ${backboard.x}mm x ${backboard.y}mm"
    println "Mat border: ${Math.round(backboard.mat_border)}mm (bottom: ${Math.round(backboard.frame_info.mat_bottom)}mm)"
}

/**
 * Prints a table of multiple size examples
 */
def printMultipleSizeExamples() {
    println "\n--- Multiple Size Examples ---"
    [[100, 150], [250, 150], [300, 200], [400, 300]].each { dims ->
        def test = calculateBackboardSize(dims[0], dims[1])
        println "${dims[0]}x${dims[1]}mm artwork => ${test.x}x${test.y}mm backboard"
    }
}
