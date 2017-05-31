package com.sinoautodiagnoseos.entity.CarBrands;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Lanye on 2017/3/2.
 */

public class CarBrands implements Serializable{
    public Map<String, List<Brand>> getData() {
        return data;
    }

    public void setData(Map<String, List<Brand>> data) {
        this.data = data;
    }

    public Map<String,List<Brand>>data;
//    public List<Brands> data;
//
//    public void setData(List<Brands> data) {
//        this.data = data;
//    }
//
//    public List<Brands> getData() {
//
//        return data;
//    }
//    public class Brands {
//        List<Brand> A;
//        List<Brand> B;
//        List<Brand> C;
//        List<Brand> D;
//        List<Brand> E;
//        List<Brand> F;
//        List<Brand> G;
//        List<Brand> H;
//        List<Brand> I;
//        List<Brand> J;
//        List<Brand> K;
//        List<Brand> L;
//        List<Brand> M;
//        List<Brand> N;
//        List<Brand> O;
//        List<Brand> P;
//        List<Brand> Q;
//        List<Brand> R;
//        List<Brand> S;
//        List<Brand> T;
//        List<Brand> U;
//        List<Brand> V;
//        List<Brand> W;
//        List<Brand> X;
//        List<Brand> Y;
//        List<Brand> Z;
//
//        public void setA(List<Brand> a) {
//            A = a;
//        }
//
//        public void setB(List<Brand> b) {
//            B = b;
//        }
//
//        public void setC(List<Brand> c) {
//            C = c;
//        }
//
//        public void setD(List<Brand> d) {
//            D = d;
//        }
//
//        public void setE(List<Brand> e) {
//            E = e;
//        }
//
//        public void setF(List<Brand> f) {
//            F = f;
//        }
//
//        public void setG(List<Brand> g) {
//            G = g;
//        }
//
//        public void setH(List<Brand> h) {
//            H = h;
//        }
//
//        public void setI(List<Brand> i) {
//            I = i;
//        }
//
//        public void setJ(List<Brand> j) {
//            J = j;
//        }
//
//        public void setK(List<Brand> k) {
//            K = k;
//        }
//
//        public void setL(List<Brand> l) {
//            L = l;
//        }
//
//        public void setM(List<Brand> m) {
//            M = m;
//        }
//
//        public void setN(List<Brand> n) {
//            N = n;
//        }
//
//        public void setO(List<Brand> o) {
//            O = o;
//        }
//
//        public void setP(List<Brand> p) {
//            P = p;
//        }
//
//        public void setQ(List<Brand> q) {
//            Q = q;
//        }
//
//        public void setR(List<Brand> r) {
//            R = r;
//        }
//
//        public void setS(List<Brand> s) {
//            S = s;
//        }
//
//        public void setT(List<Brand> t) {
//            T = t;
//        }
//
//        public void setU(List<Brand> u) {
//            U = u;
//        }
//
//        public void setV(List<Brand> v) {
//            V = v;
//        }
//
//        public void setW(List<Brand> w) {
//            W = w;
//        }
//
//        public void setX(List<Brand> x) {
//            X = x;
//        }
//
//        public void setY(List<Brand> y) {
//            Y = y;
//        }
//
//        public void setZ(List<Brand> z) {
//            Z = z;
//        }
//
//        public List<Brand> getA() {
//            return A;
//        }
//
//        public List<Brand> getB() {
//            return B;
//        }
//
//        public List<Brand> getC() {
//            return C;
//        }
//
//        public List<Brand> getD() {
//            return D;
//        }
//
//        public List<Brand> getE() {
//            return E;
//        }
//
//        public List<Brand> getF() {
//            return F;
//        }
//
//        public List<Brand> getG() {
//            return G;
//        }
//
//        public List<Brand> getH() {
//            return H;
//        }
//
//        public List<Brand> getI() {
//            return I;
//        }
//
//        public List<Brand> getJ() {
//            return J;
//        }
//
//        public List<Brand> getK() {
//            return K;
//        }
//
//        public List<Brand> getL() {
//            return L;
//        }
//
//        public List<Brand> getM() {
//            return M;
//        }
//
//        public List<Brand> getN() {
//            return N;
//        }
//
//        public List<Brand> getO() {
//            return O;
//        }
//
//        public List<Brand> getP() {
//            return P;
//        }
//
//        public List<Brand> getQ() {
//            return Q;
//        }
//
//        public List<Brand> getR() {
//            return R;
//        }
//
//        public List<Brand> getS() {
//            return S;
//        }
//
//        public List<Brand> getT() {
//            return T;
//        }
//
//        public List<Brand> getU() {
//            return U;
//        }
//
//        public List<Brand> getV() {
//            return V;
//        }
//
//        public List<Brand> getW() {
//            return W;
//        }
//
//        public List<Brand> getX() {
//            return X;
//        }
//
//        public List<Brand> getY() {
//            return Y;
//        }
//
//        public List<Brand> getZ() {
//            return Z;
//        }

        public class Brand  implements Serializable {
            public String value;
            public String text;

            public String getValue() {
                return value;
            }

            public String getText() {
                return text;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
//    }
}
