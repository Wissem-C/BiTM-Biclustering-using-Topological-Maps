package utils

import java.io._
import org.apache.spark.rdd.RDD
import  global.AbstractModel




object WriterClusters {
  def js(data: RDD[NamedVector], model: AbstractModel, path: String) = {
    val writer = new PrintWriter(new File(path))

    val dataArray = data.collect()
    var str = "var dataset = ["

    dataArray.foreach {d =>
      val closestNeuron = model.findClosestPrototype(d.toVector())
      if (d != dataArray.head) str += ','
      str += d.toJSON(closestNeuron.id)
    }

    /*model.foreach{proto =>
      str += ','
      str += "{"
      for (i <- 0 until proto._point.length) {
        str += "attr"+i+":"+proto._point(i)+", "
      }
      str += "cls:\"proto\", "
      str += "clusterId:-"+proto.id
      str += "}\n"
    }
    */
    str += "];"
    writer.write(str)

    writer.close()
  }
}
