// run initialize once the html has loaded
 document.addEventListener("DOMContentLoaded", initialize, false);

function initialize() {
    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    context.beginPath();
    context.lineWidth=10;
    context.moveTo(20, 20);
    context.lineTo(100, 100);
    context.stroke();
    context.fillStyle = "red";
    context.fill();
}


var canvas = document.getElementById('canvas');
canvas.width = 3309;
canvas.height = 2339;



var floorplan = new Image;

var xOffset = 452; // or 452
var yOffset = 979; // or 979
var originalgridspacing = 17.9; // fixed: the grid spacing of the image
var gridspacing = 8 * originalgridspacing; // 5 or 8
var RPdist = 8;
var xMax = 15;
var yMax = 6;

var lWidth = 2;
var lColour = '#0000ff';

var RPs = [{x:0, y:0}, {x:1, y:0}, {x:2, y:0}, {x:3, y:0}, {x:4, y:0}, {x:5, y:0}, {x:6, y:0}, {x:7, y:0}, 
                {x:8, y:0}, {x:9, y:0}, {x:10, y:0}, {x:11, y:0}, {x:12, y:0}, {x:13, y:0}, {x:14, y:0}, {x:15, y:0},
           {x:0, y:1}, {x:1, y:1}, {x:2, y:1}, {x:3, y:1}, {x:4, y:1}, {x:5, y:1}, {x:6, y:1}, {x:7, y:1}, 
                {x:8, y:1}, {x:9, y:1}, {x:10, y:1}, {x:11, y:1}, {x:12, y:1}, {x:13, y:1}, {x:14, y:1}, {x:15, y:1},
           {x:0, y:2}, {x:1, y:2}, {x:2, y:2}, {x:3, y:2}, {x:4, y:2}, {x:5, y:2}, {x:6, y:2}, {x:7, y:2}, 
                {x:8, y:2}, {x:9, y:2}, {x:10, y:2}, {x:11, y:2}, {x:12, y:2}, {x:13, y:2}, {x:14, y:2}, {x:15, y:2},
           {x:0, y:3}, {x:1, y:3}, {x:2, y:3}, {x:3, y:3}, {x:4, y:3}, {x:5, y:3}, {x:6, y:3}, {x:7, y:3}, 
                {x:8, y:3}, {x:9, y:3}, {x:10, y:3}, {x:11, y:3}, {x:12, y:3}, {x:13, y:3}, {x:14, y:3}, {x:15, y:3},
           {x:0, y:4}, {x:1, y:4}, {x:2, y:4}, {x:3, y:4}, {x:4, y:4}, {x:5, y:4}, {x:6, y:4}, {x:7, y:4}, 
                {x:8, y:4}, {x:9, y:4}, {x:10, y:4}, {x:11, y:4}, {x:12, y:4}, {x:13, y:4}, {x:14, y:4}, {x:15, y:4},
           {x:0, y:5}, {x:1, y:5}, {x:2, y:5}, {x:3, y:5}, {x:4, y:5}, {x:5, y:5}, {x:6, y:5}, {x:7, y:5}, 
                {x:8, y:5}, {x:9, y:5}, {x:10, y:5}, {x:11, y:5}, {x:12, y:5}, {x:13, y:5}, {x:14, y:5}, {x:15, y:5},
           {x:0, y:6}, {x:1, y:6}, {x:2, y:6}, {x:3, y:6}, {x:4, y:6}, {x:5, y:6}, {x:6, y:6}, {x:7, y:6}, 
                {x:8, y:6}, {x:9, y:6}, {x:10, y:6}, {x:11, y:6}, {x:12, y:6}, {x:13, y:6}, {x:14, y:6}, {x:15, y:6},
          ];


var totalRPs = RPs.length;
rpColour = '#000099';

var font = "10px Arial";

window.onload = function() {

    var ctx = canvas.getContext('2d');
    trackTransforms(ctx);
    var w,h;
    
    $("body").mousemove(function(e) {
                        document.Form1.posx.value = e.pageX;
                        document.Form1.posy.value = e.pageY;
                        //console.log("W"+w);
                        //console.log("H"+h);
                        })
    
    function redraw() {
        
        // Clear the entire canvas
        var p1 = ctx.transformedPoint(0, 0);
        var p2 = ctx.transformedPoint(canvas.width, canvas.height);
        ctx.clearRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
        

        ctx.save();
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.restore();
        
        // draw image
        h = ctx.height;
        w = ctx.canvas.clientWidth;
        ctx.drawImage(floorplan, 0, 0);
             
        ctx.lineWidth=lWidth;
        if (false){
            // draw grid
            for (x = 0; x < xMax + 1; x++) {
                ctx.beginPath();
                ctx.moveTo(xOffset + x * gridspacing, yOffset);
                ctx.lineTo(xOffset + x * gridspacing, yOffset + gridspacing * yMax);
                ctx.strokeStyle = lColour;
                ctx.stroke();
            }
            for (y = 0; y < yMax + 1; y++) {
                ctx.beginPath();
                ctx.moveTo(xOffset, yOffset + y * gridspacing);
                ctx.lineTo(xOffset + gridspacing * xMax, yOffset + y * gridspacing);
                ctx.strokeStyle = lColour;
                ctx.stroke();
            }
        }
        
        // DT
        ctx.fillStyle = '#990000';
        ctx.strokeStyle = '#990000';
        ctx.beginPath();
        ctx.moveTo(xOffset + 88/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 88/RPdist * gridspacing, yOffset + 32/RPdist * gridspacing);
        ctx.lineTo(xOffset + 96/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 88/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 89/RPdist * gridspacing, yOffset + 25/RPdist * gridspacing);
        ctx.lineTo(xOffset + 89/RPdist * gridspacing, yOffset + 30/RPdist * gridspacing);
        ctx.lineTo(xOffset + 94/RPdist * gridspacing, yOffset + 25/RPdist * gridspacing);
        ctx.lineTo(xOffset + 89/RPdist * gridspacing, yOffset + 25/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 90/RPdist * gridspacing, yOffset + 26/RPdist * gridspacing);
        ctx.lineTo(xOffset + 90/RPdist * gridspacing, yOffset + 28/RPdist * gridspacing);
        ctx.lineTo(xOffset + 92/RPdist * gridspacing, yOffset + 26/RPdist * gridspacing);
        ctx.lineTo(xOffset + 90/RPdist * gridspacing, yOffset + 26/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 90.7/RPdist * gridspacing, yOffset + 26.7/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        
        // OC
        ctx.beginPath();
        ctx.arc(xOffset + 72/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,10,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 80/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,20,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 72/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,20,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 80/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,40,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 72/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,30,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 80/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,60,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 72/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,40,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 80/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,80,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 72/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,50,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 80/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,100,0,2*Math.PI);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 74.6/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        
        // WC 10, 20, 30, 40
        ctx.beginPath();
        ctx.moveTo(xOffset + 56/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 64/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 64/RPdist * gridspacing, yOffset + 32/RPdist * gridspacing);
        ctx.lineTo(xOffset + 56/RPdist * gridspacing, yOffset + 32/RPdist * gridspacing);
        ctx.lineTo(xOffset + 56/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 56/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 57/RPdist * gridspacing, yOffset + 25/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 64/RPdist * gridspacing, yOffset + 24/RPdist * gridspacing);
        ctx.lineTo(xOffset + 62/RPdist * gridspacing, yOffset + 26/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 56/RPdist * gridspacing, yOffset + 32/RPdist * gridspacing);
        ctx.lineTo(xOffset + 59/RPdist * gridspacing, yOffset + 29/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(xOffset + 64/RPdist * gridspacing, yOffset + 32/RPdist * gridspacing);
        ctx.lineTo(xOffset + 60/RPdist * gridspacing, yOffset + 28/RPdist * gridspacing);
        ctx.stroke();
        ctx.beginPath();
        ctx.arc(xOffset + 59.5/RPdist * gridspacing, yOffset + 27/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        
        // draw reference points
        for(var i = 0; i < totalRPs; i++) {
            ctx.beginPath();
            var x = (RPs[i].x)*RPdist, y = (RPs[i].y)*RPdist;
            var xCoord = xOffset + RPs[i].x * gridspacing;
            var yCoord = yOffset + RPs[i].y * gridspacing;
            ctx.arc(xCoord,yCoord,4,0,2*Math.PI);
            ctx.strokeStyle = lColour;
            ctx.fillStyle = rpColour;
            ctx.fill();
            //ctx.stroke();  
            ctx.font = font;
            ctx.fillText('('+x+','+y+')',xCoord+10,yCoord-10);
        }
        
        ctx.beginPath();
        ctx.arc(xOffset + 57/RPdist * gridspacing, yOffset + 25/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        ctx.beginPath();
        ctx.arc(xOffset + 62/RPdist * gridspacing, yOffset + 26/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        ctx.beginPath();
        ctx.arc(xOffset + 59/RPdist * gridspacing, yOffset + 29/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        ctx.beginPath();
        ctx.arc(xOffset + 60/RPdist * gridspacing, yOffset + 28/RPdist * gridspacing,3,0,2*Math.PI);
        ctx.fill();
        

        
    }
    
    redraw();
    
//width:3309px;
//height:2339px;
    
    var panXScale = 1/(3309/800);
    var panYScale = 2339/600;
    
    var maxX = 3309,
    minX = 0,
    maxY = 2339,
    minY = 0,
    testX = 10,
    testY = 10;
    
    var lastX = canvas.width / 2;
    var lastY = canvas.height / 2;
    //console.log ( 'canvas.width: '+ canvas.width);
    //console.log ( 'canvas.height: '+ canvas.height);
    
    var dragStart, dragged;
    
    canvas.addEventListener('mousedown', function(evt) {
                            document.body.style.mozUserSelect = document.body.style.webkitUserSelect = document.body.style.userSelect = 'none';
                            lastX = evt.offsetX || (evt.pageX - canvas.offsetLeft);
                            lastY = evt.offsetY || (evt.pageY - canvas.offsetTop);
                            dragStart = ctx.transformedPoint(lastX, lastY);
                            dragged = false;
                            }, false);
    
    canvas.addEventListener('mousemove', function(evt) {
                            lastX = evt.offsetX || (evt.pageX - canvas.offsetLeft);
                            lastY = evt.offsetY || (evt.pageY - canvas.offsetTop);
                            dragged = true;
                            if (dragStart) {
                            var pt = ctx.transformedPoint(lastX, lastY);
                            ctx.translate(pt.x - dragStart.x, pt.y - dragStart.y);
                            console.log ( 'lastX: '+ pt);
                            redraw();
                            }
                            }, false);
    
    canvas.addEventListener('mouseup', function(evt) {
                        dragStart = null;
                            if (!dragged) zoom(evt.shiftKey ? -1 : 1);
                            }, false);
    
    var scaleFactor = 1.1;
    
    var zoom = function(clicks) {
        var pt = ctx.transformedPoint(lastX, lastY);
        ctx.translate(pt.x, pt.y);
        var factor = Math.pow(scaleFactor, clicks);
        ctx.scale(factor, factor);
        ctx.translate(-pt.x, -pt.y);
        redraw();
    }
    
    var handleScroll = function(evt) {
        var delta = evt.wheelDelta ? evt.wheelDelta / 40 : evt.detail ? -evt.detail : 0;
        if (delta) zoom(delta);
        return evt.preventDefault() && false;
    };
    
    canvas.addEventListener('DOMMouseScroll', handleScroll, false);
    canvas.addEventListener('mousewheel', handleScroll, false);
};

floorplan.src = 'Floor4.png';

// Adds ctx.getTransform() - returns an SVGMatrix
// Adds ctx.transformedPoint(x,y) - returns an SVGPoint
function trackTransforms(ctx) {
    console.log ('trackTransforms');
    var svg = document.createElementNS("http://www.w3.org/2000/svg", 'svg');
    var xform = svg.createSVGMatrix();
    ctx.getTransform = function() {
        return xform;
    };
    
    var savedTransforms = [];
    var save = ctx.save;
    ctx.save = function() {
        savedTransforms.push(xform.translate(0, 0));
        return save.call(ctx);
    };
    
    var restore = ctx.restore;
    ctx.restore = function() {
        xform = savedTransforms.pop();
        return restore.call(ctx);
    };
    
    var scale = ctx.scale;
    ctx.scale = function(sx, sy) {
        xform = xform.scaleNonUniform(sx, sy);
        return scale.call(ctx, sx, sy);
    };
    
    var rotate = ctx.rotate;
    ctx.rotate = function(radians) {
        xform = xform.rotate(radians * 180 / Math.PI);
        return rotate.call(ctx, radians);
    };
    
    var translate = ctx.translate;
    ctx.translate = function(dx, dy) {
        xform = xform.translate(dx, dy);
        return translate.call(ctx, dx, dy);
    };
    
    var transform = ctx.transform;
    ctx.transform = function(a, b, c, d, e, f) {
        var m2 = svg.createSVGMatrix();
        m2.a = a;
        m2.b = b;
        m2.c = c;
        m2.d = d;
        m2.e = e;
        m2.f = f;
        xform = xform.multiply(m2);
        return transform.call(ctx, a, b, c, d, e, f);
    };
    
    var setTransform = ctx.setTransform;
    ctx.setTransform = function(a, b, c, d, e, f) {
        xform.a = a;
        xform.b = b;
        xform.c = c;
        xform.d = d;
        xform.e = e;
        xform.f = f;
        return setTransform.call(ctx, a, b, c, d, e, f);
    };
    
    var pt = svg.createSVGPoint();
    ctx.transformedPoint = function(x, y) {
        pt.x = x;
        pt.y = y;
        return pt.matrixTransform(xform.inverse());
    }
}