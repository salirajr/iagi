package com.rj.sysinvest.layout;

import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutTemplateInfo {

//{
//    "id": "layout-01",
//    "description": "some description",
//    "templatePath": "/path/to/file",
//    "templateType": "svg",
//    "sitePoint" : [1,2],
//    "towerPoint" : [1,3],
//    "levelPoint" : [99,33],    
//    "rooms": [
//        {
//            "positionId": "01",
//            "area": [[1, 22], [3, 12], [33, 213]]
//        }
//    ]
//}
    private String id,
            description,
            templatePath,
            templateType;

    private int[] sitePoint, towerPoint, levelPoint;

    private List<LayoutRoom> rooms;

    public Optional<LayoutTemplateInfo.LayoutRoom> findLayoutRoomByPositionId(String positionId) {
        return getRooms().stream()
                .filter(roomArea -> roomArea.getPositionId().equals(positionId))
                .findFirst();
    }

    @Data
    public static class LayoutRoom {

        private String positionId;
        private int[][] area;

        public Polygon toPolygon() {
            Polygon polygon = new Polygon();
            for (int[] xy : getArea()) {
                polygon.addPoint(xy[0], xy[1]);
            }
            return polygon;
        }
    }

}
