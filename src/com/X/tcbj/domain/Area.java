package com.X.tcbj.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class Area implements Serializable {
    private List<Province> list;
    private int status;
    private String statusMsg;

    public List<Province> getList() {
        return list;
    }

    public void setList(List<Province> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public static class Province implements Serializable{
        private List<Citys> Citys;
        private String ClassList;
        private int ID;
        private int Level;
        private String Name;
        private int ParentID;

        public List<Province.Citys> getCitys() {
            return Citys;
        }

        public void setCitys(List<Province.Citys> citys) {
            Citys = citys;
        }

        public String getClassList() {
            return ClassList;
        }

        public void setClassList(String classList) {
            ClassList = classList;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getParentID() {
            return ParentID;
        }

        public void setParentID(int parentID) {
            ParentID = parentID;
        }

        public static class Citys implements Serializable{
            private String ClassList;
            private List<Countys> Countys;
            private int ID;
            private int Level;
            private String Name;
            private int ParentID;

            public String getClassList() {
                return ClassList;
            }

            public void setClassList(String classList) {
                ClassList = classList;
            }

            public List<Province.Citys.Countys> getCountys() {
                return Countys;
            }

            public void setCountys(List<Province.Citys.Countys> countys) {
                Countys = countys;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public int getLevel() {
                return Level;
            }

            public void setLevel(int level) {
                Level = level;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public int getParentID() {
                return ParentID;
            }

            public void setParentID(int parentID) {
                ParentID = parentID;
            }

            public static class Countys implements Serializable{
                private String ClassList;
                private int ID;
                private int Level;
                private String Name;
                private int ParentID;

                public String getClassList() {
                    return ClassList;
                }

                public void setClassList(String classList) {
                    ClassList = classList;
                }

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public int getLevel() {
                    return Level;
                }

                public void setLevel(int level) {
                    Level = level;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String name) {
                    Name = name;
                }

                public int getParentID() {
                    return ParentID;
                }

                public void setParentID(int parentID) {
                    ParentID = parentID;
                }
            }
        }
    }


}
